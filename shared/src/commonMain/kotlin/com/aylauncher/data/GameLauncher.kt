package com.aylauncher.data

import com.aylauncher.domain.Account
import com.aylauncher.domain.Instance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Launches an installed Minecraft version.
 * Desktop: real Java process.
 * Android: not supported in this step (needs Pojav-style runtime).
 */

sealed class LaunchState {
    data object Idle : LaunchState()
    data class Starting(val instanceName: String) : LaunchState()
    data class Running(val instanceName: String, val pid: Long?) : LaunchState()
    data class Exited(val instanceName: String, val code: Int?) : LaunchState()
    data class Error(val message: String) : LaunchState()
}

data class LaunchRequest(
    val instance: Instance,
    val account: Account?,
    val versionJsonPath: String,
    val clientJarPath: String,
    val librariesDir: String,
    val gameDir: String,
    val nativesDir: String,
    val assetsDir: String,
    val javaPath: String?,
    val ramMb: Int
)

interface ProcessLauncher {
    /**
     * Starts the game process. Returns a handle id / pid if available.
     * Throws on failure to start.
     */
    fun start(
        javaPath: String,
        jvmArgs: List<String>,
        mainClass: String,
        gameArgs: List<String>,
        workingDir: String
    ): Long?

    fun findJava(): String?
}

expect fun createProcessLauncher(): ProcessLauncher

class GameLauncher {
    private val processLauncher = createProcessLauncher()
    private val _state = MutableStateFlow<LaunchState>(LaunchState.Idle)
    val state: StateFlow<LaunchState> = _state.asStateFlow()

    private val dataRoot: String by lazy { createFileBrowser().rootPath }

    suspend fun launch(instance: Instance, account: Account?) {
        if (_state.value is LaunchState.Starting || _state.value is LaunchState.Running) {
            _state.value = LaunchState.Error("A game is already starting or running")
            return
        }
        _state.value = LaunchState.Starting(instance.name)
        try {
            val versionId = instance.minecraftVersion
            val versionDir = "$dataRoot/versions/$versionId"
            val clientJar = "$versionDir/$versionId.jar"
            val versionJson = "$versionDir/$versionId.json"
            val librariesDir = "$dataRoot/libraries"
            val gameDir = "$dataRoot/instances/${instance.id}"
            val nativesDir = "$versionDir/natives"
            val assetsDir = "$dataRoot/assets"

            if (!fileExistsAndNonEmpty(clientJar)) {
                _state.value = LaunchState.Error(
                    "Client jar missing. Use Install on this instance first."
                )
                return
            }

            ensureParentDir("$gameDir/placeholder")
            // ensure dirs exist
            ensureParentDir("$nativesDir/.keep")
            ensureParentDir("$assetsDir/.keep")

            val java = processLauncher.findJava()
                ?: run {
                    _state.value = LaunchState.Error(
                        "No Java found. Set JAVA_HOME or install a JDK 17+."
                    )
                    return
                }

            val mainClass = readMainClass(versionJson) ?: "net.minecraft.client.main.Main"
            val classpath = buildClasspath(clientJar, librariesDir, versionJson)

            val ram = instance.ramMb.coerceIn(1024, 32768)
            val username = account?.username ?: "Player"
            val uuid = account?.uuid ?: "00000000-0000-0000-0000-000000000000"
            val accessToken = account?.accessToken ?: "0"
            val userType = if (account?.type?.name == "MICROSOFT") "msa" else "legacy"

            val jvmArgs = listOf(
                "-Xmx${ram}M",
                "-Xms${(ram / 2).coerceAtLeast(512)}M",
                "-Djava.library.path=$nativesDir",
                "-cp",
                classpath
            )

            val gameArgs = listOf(
                "--username", username,
                "--version", versionId,
                "--gameDir", gameDir,
                "--assetsDir", assetsDir,
                "--assetIndex", versionId, // simplified; real index id from JSON later
                "--uuid", uuid,
                "--accessToken", accessToken,
                "--userType", userType,
                "--versionType", "AY-Launcher"
            )

            val pid = processLauncher.start(
                javaPath = java,
                jvmArgs = jvmArgs,
                mainClass = mainClass,
                gameArgs = gameArgs,
                workingDir = gameDir
            )
            _state.value = LaunchState.Running(instance.name, pid)
        } catch (e: Exception) {
            _state.value = LaunchState.Error((e.message ?: "Launch failed").take(200))
        }
    }

    fun reset() {
        _state.value = LaunchState.Idle
    }

    private fun buildClasspath(clientJar: String, librariesDir: String, versionJsonPath: String): String {
        val sep = classpathSeparator()
        val libs = listLibraryJars(librariesDir, versionJsonPath)
        return (libs + clientJar).joinToString(sep)
    }
}

/** Platform helpers for launch */
expect fun classpathSeparator(): String
expect fun listLibraryJars(librariesDir: String, versionJsonPath: String): List<String>
expect fun readMainClass(versionJsonPath: String): String?
