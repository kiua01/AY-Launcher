package com.aylauncher.data

import java.io.File

actual fun createFileBrowser(): FileBrowser = DesktopFileBrowser()

class DesktopFileBrowser : FileBrowser {
    override val rootPath: String by lazy {
        val home = System.getProperty("user.home") ?: "."
        // Prefer existing .minecraft, otherwise launcher dir
        val mc = File(home, ".minecraft")
        val ay = File(home, ".aylauncher")
        when {
            mc.exists() && mc.isDirectory -> mc.absolutePath
            else -> {
                ay.mkdirs()
                // Seed a few folders so the UI isn't empty
                listOf("mods", "resourcepacks", "shaderpacks", "saves", "screenshots").forEach {
                    File(ay, it).mkdirs()
                }
                File(ay, "options.txt").takeIf { !it.exists() }?.writeText("# AY Launcher\n")
                ay.absolutePath
            }
        }
    }

    override fun isUnderRoot(path: String): Boolean {
        val root = File(rootPath).canonicalFile
        val target = File(path).canonicalFile
        return target.path == root.path || target.path.startsWith(root.path + File.separator)
    }

    override fun list(path: String): List<FsEntry> {
        val dir = File(path)
        if (!dir.exists() || !dir.isDirectory || !isUnderRoot(path)) return emptyList()
        return dir.listFiles()
            ?.map { f ->
                FsEntry(
                    name = f.name,
                    path = f.absolutePath,
                    isDirectory = f.isDirectory,
                    sizeBytes = if (f.isFile) f.length() else 0L,
                    lastModified = f.lastModified()
                )
            }
            ?.sortedWith(compareBy<FsEntry> { !it.isDirectory }.thenBy { it.name.lowercase() })
            ?: emptyList()
    }

    override fun parent(path: String): String? {
        if (!isUnderRoot(path)) return null
        val p = File(path).parentFile ?: return null
        return if (isUnderRoot(p.absolutePath)) p.absolutePath else null
    }

    override fun exists(path: String): Boolean =
        isUnderRoot(path) && File(path).exists()
}
