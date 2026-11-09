package com.aylauncher.data

import java.io.File
import java.util.concurrent.TimeUnit

actual fun createProcessLauncher(): ProcessLauncher = DesktopProcessLauncher()

actual fun classpathSeparator(): String = File.pathSeparator

actual fun listLibraryJars(librariesDir: String, versionJsonPath: String): List<String> {
    val root = File(librariesDir)
    if (!root.exists()) return emptyList()
    // Collect all jars under libraries (simple; later filter by version JSON only)
    return root.walkTopDown()
        .filter { it.isFile && it.extension.equals("jar", ignoreCase = true) }
        .map { it.absolutePath }
        .toList()
}

actual fun readMainClass(versionJsonPath: String): String? {
    val f = File(versionJsonPath)
    if (!f.exists()) return null
    val text = f.readText()
    // Minimal parse without full re-serialization
    val key = "\"mainClass\""
    val idx = text.indexOf(key)
    if (idx < 0) return null
    val after = text.substring(idx + key.length)
    val start = after.indexOf('"')
    if (start < 0) return null
    val end = after.indexOf('"', start + 1)
    if (end < 0) return null
    return after.substring(start + 1, end)
}

class DesktopProcessLauncher : ProcessLauncher {
    override fun findJava(): String? {
        val home = System.getenv("JAVA_HOME")
        if (!home.isNullOrBlank()) {
            val bin = File(home, "bin/java")
            val binExe = File(home, "bin/java.exe")
            when {
                bin.exists() -> return bin.absolutePath
                binExe.exists() -> return binExe.absolutePath
            }
        }
        // Fallback: java on PATH
        return try {
            val pb = ProcessBuilder("java", "-version")
            pb.redirectErrorStream(true)
            val p = pb.start()
            p.waitFor(3, TimeUnit.SECONDS)
            if (p.exitValue() == 0 || p.exitValue() == 1) "java" else null
            // -version often exits 0; some JREs use stderr
            "java"
        } catch (_: Exception) {
            null
        }
    }

    override fun start(
        javaPath: String,
        jvmArgs: List<String>,
        mainClass: String,
        gameArgs: List<String>,
        workingDir: String
    ): Long? {
        File(workingDir).mkdirs()
        val cmd = mutableListOf(javaPath)
        cmd.addAll(jvmArgs)
        cmd.add(mainClass)
        cmd.addAll(gameArgs)

        val pb = ProcessBuilder(cmd)
        pb.directory(File(workingDir))
        pb.redirectErrorStream(true)
        // Keep output available for later log viewing
        val logFile = File(workingDir, "latest_launch.log")
        pb.redirectOutput(ProcessBuilder.Redirect.appendTo(logFile))

        val process = pb.start()
        // Detach – don't wait. Return PID if available (Java 9+)
        return try {
            process.pid()
        } catch (_: Throwable) {
            null
        }
    }
}
