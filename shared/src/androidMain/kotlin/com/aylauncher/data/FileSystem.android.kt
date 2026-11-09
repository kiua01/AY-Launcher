package com.aylauncher.data

/**
 * Android stub – shows a fake but realistic tree until we inject Context
 * and use getExternalFilesDir / scoped storage safely.
 */
actual fun createFileBrowser(): FileBrowser = object : FileBrowser {
    override val rootPath: String = "/storage/emulated/0/games/AYLauncher"

    private val fake = mapOf(
        rootPath to listOf(
            FsEntry(".minecraft", "$rootPath/.minecraft", true),
            FsEntry("mods", "$rootPath/mods", true, 0),
            FsEntry("resourcepacks", "$rootPath/resourcepacks", true),
            FsEntry("shaderpacks", "$rootPath/shaderpacks", true),
            FsEntry("options.txt", "$rootPath/options.txt", false, 4096),
            FsEntry("servers.dat", "$rootPath/servers.dat", false, 12288),
        ),
        "$rootPath/mods" to listOf(
            FsEntry("example-mod.jar", "$rootPath/mods/example-mod.jar", false, 2_100_000)
        ),
        "$rootPath/.minecraft" to listOf(
            FsEntry("saves", "$rootPath/.minecraft/saves", true),
            FsEntry("logs", "$rootPath/.minecraft/logs", true)
        )
    )

    override fun list(path: String): List<FsEntry> = fake[path] ?: emptyList()

    override fun parent(path: String): String? {
        if (path == rootPath) return null
        val idx = path.lastIndexOf('/')
        return if (idx > 0) path.substring(0, idx) else rootPath
    }

    override fun exists(path: String): Boolean = path == rootPath || fake.containsKey(path) ||
        fake.values.any { list -> list.any { it.path == path } }

    override fun isUnderRoot(path: String): Boolean = path.startsWith(rootPath)
}
