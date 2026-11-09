package com.aylauncher.data

import java.io.File

actual fun createLauncherStorage(): LauncherStorage = DesktopLauncherStorage()

class DesktopLauncherStorage : LauncherStorage {
    private val dir: File by lazy {
        val home = System.getProperty("user.home") ?: "."
        File(home, ".aylauncher").also { it.mkdirs() }
    }

    private val file: File by lazy { File(dir, "state.json") }

    override fun load(): PersistedState? {
        if (!file.exists()) return null
        val text = file.readText()
        if (text.isBlank()) return null
        return Persistence.decode(text)
    }

    override fun save(state: PersistedState) {
        dir.mkdirs()
        file.writeText(Persistence.encode(state))
    }
}
