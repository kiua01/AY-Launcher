package com.aylauncher.data

import com.aylauncher.domain.Account
import com.aylauncher.domain.Instance
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Multiplatform persistence for accounts + instances.
 * Desktop: real JSON files under user home.
 * Android: in-memory for now (hooks ready; Context-based storage later).
 */

@Serializable
data class PersistedState(
    val accounts: List<Account> = emptyList(),
    val activeAccountId: String? = null,
    val instances: List<Instance> = emptyList()
)

interface LauncherStorage {
    fun load(): PersistedState?
    fun save(state: PersistedState)
}

expect fun createLauncherStorage(): LauncherStorage

private val json = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
    encodeDefaults = true
}

object Persistence {
    private val storage by lazy { createLauncherStorage() }

    fun loadIntoAppState() {
        try {
            val data = storage.load() ?: return
            // Restore accounts
            data.accounts.forEach { acc ->
                when (acc.type) {
                    com.aylauncher.domain.AccountType.OFFLINE -> {
                        // Use repository methods carefully to avoid duplicate side-effects
                        if (AppState.accounts.accounts.value.none { it.id == acc.id }) {
                            // Direct restore path
                            AppState.accounts.restore(acc)
                        }
                    }
                    com.aylauncher.domain.AccountType.MICROSOFT -> {
                        if (AppState.accounts.accounts.value.none { it.id == acc.id }) {
                            AppState.accounts.restore(acc.copy(accessToken = null))
                        }
                    }
                }
            }
            data.activeAccountId?.let { id ->
                AppState.accounts.accounts.value.find { it.id == id }?.let {
                    AppState.accounts.setActive(it)
                }
            }
            data.instances.forEach { inst ->
                if (AppState.instances.instances.value.none { it.id == inst.id }) {
                    AppState.instances.restore(inst)
                }
            }
        } catch (_: Exception) {
            // Corrupt or missing file – start clean
        }
    }

    fun saveFromAppState() {
        try {
            val state = PersistedState(
                accounts = AppState.accounts.accounts.value.map { it.copy(accessToken = null) },
                activeAccountId = AppState.accounts.activeAccount.value?.id,
                instances = AppState.instances.instances.value
            )
            storage.save(state)
        } catch (_: Exception) {
            // Best-effort; do not crash UI
        }
    }

    fun encode(state: PersistedState): String = json.encodeToString(state)
    fun decode(text: String): PersistedState = json.decodeFromString(text)
}
