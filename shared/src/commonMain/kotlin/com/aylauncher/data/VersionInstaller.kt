package com.aylauncher.data

import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * Downloads a Minecraft version (client jar + libraries) into the launcher data folder.
 * Assets index download is prepared but kept lighter for this step.
 *
 * Careful design:
 * - User must explicitly start an install
 * - Progress is reported
 * - Limited parallelism
 * - Never writes outside the launcher data root
 */

@Serializable
data class VersionDetail(
    val id: String,
    val mainClass: String = "",
    val libraries: List<Library> = emptyList(),
    val downloads: VersionDownloads? = null,
    val assetIndex: AssetIndexRef? = null,
    val minecraftArguments: String? = null
)

@Serializable
data class VersionDownloads(
    val client: DownloadInfo? = null,
    val server: DownloadInfo? = null
)

@Serializable
data class DownloadInfo(
    val sha1: String = "",
    val size: Long = 0,
    val url: String
)

@Serializable
data class Library(
    val name: String,
    val downloads: LibraryDownloads? = null,
    val rules: List<Rule>? = null,
    val natives: Map<String, String>? = null
)

@Serializable
data class LibraryDownloads(
    val artifact: Artifact? = null,
    val classifiers: Map<String, Artifact>? = null
)

@Serializable
data class Artifact(
    val path: String = "",
    val sha1: String = "",
    val size: Long = 0,
    val url: String = ""
)

@Serializable
data class Rule(
    val action: String,
    val os: OsRule? = null
)

@Serializable
data class OsRule(
    val name: String? = null
)

@Serializable
data class AssetIndexRef(
    val id: String = "",
    val sha1: String = "",
    val size: Long = 0,
    val totalSize: Long = 0,
    val url: String = ""
)

sealed class InstallState {
    data object Idle : InstallState()
    data class Preparing(val versionId: String) : InstallState()
    data class Downloading(
        val versionId: String,
        val current: String,
        val completed: Int,
        val total: Int,
        val bytesDownloaded: Long,
        val bytesTotal: Long
    ) : InstallState()
    data class Success(val versionId: String, val gameDir: String) : InstallState()
    data class Error(val message: String) : InstallState()
}

class VersionInstaller {
    private val client by lazy { createHttpClient() }
    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    private val _state = MutableStateFlow<InstallState>(InstallState.Idle)
    val state: StateFlow<InstallState> = _state.asStateFlow()

    private val dataRoot: String by lazy {
        // Reuse same root logic as file browser when possible
        createFileBrowser().rootPath
    }

    suspend fun install(versionId: String, versionUrl: String) {
        if (_state.value is InstallState.Downloading || _state.value is InstallState.Preparing) {
            return // already running
        }
        _state.value = InstallState.Preparing(versionId)
        try {
            val detail: VersionDetail = client.get(versionUrl).body()
            val tasks = mutableListOf<DownloadTask>()

            // Client jar
            detail.downloads?.client?.let { d ->
                val path = "$dataRoot/versions/$versionId/$versionId.jar"
                tasks += DownloadTask(d.url, path, d.sha1, d.size, "client.jar")
            }

            // Version JSON itself
            val jsonPath = "$dataRoot/versions/$versionId/$versionId.json"
            tasks += DownloadTask(versionUrl, jsonPath, "", 0, "$versionId.json")

            // Libraries (artifact only for now; natives later)
            for (lib in detail.libraries) {
                if (!libraryAllowed(lib)) continue
                val art = lib.downloads?.artifact ?: continue
                if (art.url.isBlank()) continue
                val path = if (art.path.isNotBlank()) {
                    "$dataRoot/libraries/${art.path}"
                } else {
                    "$dataRoot/libraries/${lib.name.replace(':', '/')}.jar"
                }
                tasks += DownloadTask(art.url, path, art.sha1, art.size, lib.name)
            }

            val totalBytes = tasks.sumOf { it.size.coerceAtLeast(0) }
            var completed = 0
            var bytesDone = 0L

            _state.value = InstallState.Downloading(
                versionId = versionId,
                current = "Starting…",
                completed = 0,
                total = tasks.size,
                bytesDownloaded = 0,
                bytesTotal = totalBytes
            )

            val semaphore = Semaphore(4) // limit parallel downloads
            coroutineScope {
                tasks.map { task ->
                    async {
                        semaphore.withPermit {
                            downloadFile(task)
                            completed++
                            bytesDone += task.size.coerceAtLeast(0)
                            _state.value = InstallState.Downloading(
                                versionId = versionId,
                                current = task.label,
                                completed = completed,
                                total = tasks.size,
                                bytesDownloaded = bytesDone,
                                bytesTotal = totalBytes
                            )
                        }
                    }
                }.awaitAll()
            }

            val gameDir = "$dataRoot/versions/$versionId"
            _state.value = InstallState.Success(versionId, gameDir)
        } catch (e: Exception) {
            _state.value = InstallState.Error(
                (e.message ?: "Install failed").take(160)
            )
        }
    }

    fun reset() {
        _state.value = InstallState.Idle
    }

    private fun libraryAllowed(lib: Library): Boolean {
        val rules = lib.rules ?: return true
        // Very simplified OS rule check – allow if no disallow for current-ish desktop
        var allowed = false
        for (rule in rules) {
            val osName = rule.os?.name
            val matches = osName == null || osName == "windows" || osName == "linux" || osName == "osx"
            if (rule.action == "allow" && matches) allowed = true
            if (rule.action == "disallow" && matches) allowed = false
        }
        return allowed
    }

    private suspend fun downloadFile(task: DownloadTask) = withContext(Dispatchers.Default) {
        // Skip if file already exists and non-empty (simple cache)
        if (fileExistsAndNonEmpty(task.path)) return@withContext

        ensureParentDir(task.path)
        val response = client.get(task.url)
        val bytes: ByteArray = response.body()
        writeBytes(task.path, bytes)
    }
}

data class DownloadTask(
    val url: String,
    val path: String,
    val sha1: String,
    val size: Long,
    val label: String
)

/** Platform file helpers used by the installer */
expect fun fileExistsAndNonEmpty(path: String): Boolean
expect fun ensureParentDir(path: String)
expect fun writeBytes(path: String, bytes: ByteArray)
