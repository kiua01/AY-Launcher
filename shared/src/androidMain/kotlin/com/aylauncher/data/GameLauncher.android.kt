package com.aylauncher.data

actual fun createProcessLauncher(): ProcessLauncher = object : ProcessLauncher {
    override fun start(
        javaPath: String,
        jvmArgs: List<String>,
        mainClass: String,
        gameArgs: List<String>,
        workingDir: String
    ): Long? {
        throw UnsupportedOperationException(
            "Game launch on Android needs a dedicated runtime (e.g. Pojav-style). Not in this step."
        )
    }

    override fun findJava(): String? = null
}

actual fun classpathSeparator(): String = ":"

actual fun listLibraryJars(librariesDir: String, versionJsonPath: String): List<String> = emptyList()

actual fun readMainClass(versionJsonPath: String): String? = null
