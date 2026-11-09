package com.aylauncher.data

/**
 * Android file writes are stubbed until we have a Context-backed data directory.
 * Install will report success paths conceptually but not persist large jars yet.
 */
actual fun fileExistsAndNonEmpty(path: String): Boolean = false

actual fun ensureParentDir(path: String) {
    // no-op stub
}

actual fun writeBytes(path: String, bytes: ByteArray) {
    // no-op stub – real implementation needs Context.getFilesDir()
}
