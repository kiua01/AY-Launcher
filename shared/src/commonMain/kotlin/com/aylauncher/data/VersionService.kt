package com.aylauncher.data

import kotlinx.serialization.Serializable

/**
 * Stub for Mojang version manifest.
 * Later this will call https://piston-meta.mojang.com/mc/game/version_manifest_v2.json
 * and cache the result. For now we provide a small static list so the UI can be developed.
 */
@Serializable
data class VersionInfo(
    val id: String,
    val type: String, // release, snapshot, old_beta, old_alpha
    val url: String = "",
    val releaseTime: String = ""
)

object VersionService {
    // Small static list for UI development
    val popularVersions = listOf(
        VersionInfo("1.21.3", "release"),
        VersionInfo("1.21.1", "release"),
        VersionInfo("1.20.6", "release"),
        VersionInfo("1.20.4", "release"),
        VersionInfo("1.19.4", "release"),
        VersionInfo("1.18.2", "release"),
        VersionInfo("1.16.5", "release"),
        VersionInfo("1.12.2", "release"),
        VersionInfo("1.8.9", "release"),
        VersionInfo("24w44a", "snapshot"),
    )

    fun releases(): List<VersionInfo> = popularVersions.filter { it.type == "release" }
    fun snapshots(): List<VersionInfo> = popularVersions.filter { it.type == "snapshot" }
}
