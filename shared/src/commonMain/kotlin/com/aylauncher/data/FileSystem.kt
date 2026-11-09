package com.aylauncher.data

/**
 * Safe multiplatform file browsing for the File Manager.
 * Desktop: real directories under a restricted root.
 * Android: stub entries (no arbitrary FS access yet).
 */

data class FsEntry(
    val name: String,
    val path: String,
    val isDirectory: Boolean,
    val sizeBytes: Long = 0L,
    val lastModified: Long = 0L
) {
    val sizeLabel: String
        get() = when {
            isDirectory -> "Folder"
            sizeBytes < 1024 -> "$sizeBytes B"
            sizeBytes < 1024 * 1024 -> "${sizeBytes / 1024} KB"
            sizeBytes < 1024L * 1024 * 1024 -> "${"%.1f".format(sizeBytes / (1024.0 * 1024))} MB"
            else -> "${"%.1f".format(sizeBytes / (1024.0 * 1024 * 1024))} GB"
        }
}

interface FileBrowser {
    /** Absolute path of the safe root (user cannot go above this). */
    val rootPath: String

    fun list(path: String): List<FsEntry>
    fun parent(path: String): String?
    fun exists(path: String): Boolean
    fun isUnderRoot(path: String): Boolean
}

expect fun createFileBrowser(): FileBrowser
