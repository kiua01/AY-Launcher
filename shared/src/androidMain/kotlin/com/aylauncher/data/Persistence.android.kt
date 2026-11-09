package com.aylauncher.data

/**
 * Android storage stub.
 * Keeps data in memory for now. Later: use application Context filesDir
 * or DataStore once the Android entrypoint can inject Context safely.
 */
actual fun createLauncherStorage(): LauncherStorage = object : LauncherStorage {
    private var memory: PersistedState? = null

    override fun load(): PersistedState? = memory

    override fun save(state: PersistedState) {
        memory = state
    }
}
