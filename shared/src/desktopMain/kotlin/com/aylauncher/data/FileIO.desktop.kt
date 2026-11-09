package com.aylauncher.data

import java.io.File

actual fun fileExistsAndNonEmpty(path: String): Boolean {
    val f = File(path)
    return f.exists() && f.isFile && f.length() > 0
}

actual fun ensureParentDir(path: String) {
    File(path).parentFile?.mkdirs()
}

actual fun writeBytes(path: String, bytes: ByteArray) {
    val f = File(path)
    f.parentFile?.mkdirs()
    f.writeBytes(bytes)
}
