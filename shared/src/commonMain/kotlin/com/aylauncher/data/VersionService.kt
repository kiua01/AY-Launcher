package com.aylauncher.data

import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Live Mojang version manifest service.
 * Fetches https://piston-meta.mojang.com/mc/game/version_manifest_v2.json
 * with caching, loading state, and safe fallbacks.
 */

@Serializable
data class VersionManifest(
    val latest: LatestVersions,
    val versions: List<ManifestVersion>
)

@Serializable
data class LatestVersions(
    val release: String,
    val snapshot: String
)

@Serializable
data class ManifestVersion(
    val id: String,
    val type: String,
    val url: String = "",
    val time: String = "",
    @SerialName("releaseTime") val releaseTime: String = "",
    val sha1: String = ""
)

sealed class VersionsState {
    data object Idle : VersionsState()
    data object Loading : VersionsState()
    data class Ready(
        val latestRelease: String,
        val latestSnapshot: String,
        val releases: List<ManifestVersion>,
        val snapshots: List<ManifestVersion>,
        val all: List<ManifestVersion>
    ) : VersionsState()
    data class Error(val message: String, val fallback: List<ManifestVersion>) : VersionsState()
}

class VersionService {
    private val client by lazy { createHttpClient() }

    private val _state = MutableStateFlow<VersionsState>(VersionsState.Idle)
    val state: StateFlow<VersionsState> = _state.asStateFlow()

    private var cached: VersionManifest? = null

    companion object {
        private const val MANIFEST_URL =
            "https://piston-meta.mojang.com/mc/game/version_manifest_v2.json"

        /** Safe static fallback if network fails */
        val FALLBACK = listOf(
            ManifestVersion("1.21.3", "release"),
            ManifestVersion("1.21.1", "release"),
            ManifestVersion("1.20.6", "release"),
            ManifestVersion("1.20.4", "release"),
            ManifestVersion("1.19.4", "release"),
            ManifestVersion("1.18.2", "release"),
            ManifestVersion("1.16.5", "release"),
            ManifestVersion("1.12.2", "release"),
            ManifestVersion("1.8.9", "release"),
        )
    }

    suspend fun refresh(force: Boolean = false) {
        if (!force && cached != null) {
            emitReady(cached!!)
            return
        }
        _state.value = VersionsState.Loading
        try {
            val manifest: VersionManifest = client.get(MANIFEST_URL).body()
            cached = manifest
            emitReady(manifest)
        } catch (e: Exception) {
            _state.value = VersionsState.Error(
                message = (e.message ?: "Failed to load versions").take(140),
                fallback = FALLBACK
            )
        }
    }

    private fun emitReady(manifest: VersionManifest) {
        val releases = manifest.versions.filter { it.type == "release" }
        val snapshots = manifest.versions.filter { it.type == "snapshot" }
        _state.value = VersionsState.Ready(
            latestRelease = manifest.latest.release,
            latestSnapshot = manifest.latest.snapshot,
            releases = releases,
            snapshots = snapshots,
            all = manifest.versions
        )
    }

    fun clearCache() {
        cached = null
        _state.value = VersionsState.Idle
    }
}
