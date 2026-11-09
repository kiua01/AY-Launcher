package com.aylauncher.data

import com.aylauncher.domain.Account
import com.aylauncher.domain.AccountType
import com.aylauncher.domain.Instance
import com.aylauncher.domain.LoaderType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * In-memory repositories for the early scaffold.
 * Later these will be backed by local storage + network (Microsoft auth, Mojang manifests, Modrinth, etc.)
 * and the Rust core for heavy file / download work.
 */

class AccountRepository {
    private val _accounts = MutableStateFlow<List<Account>>(emptyList())
    val accounts: StateFlow<List<Account>> = _accounts.asStateFlow()

    private val _active = MutableStateFlow<Account?>(null)
    val activeAccount: StateFlow<Account?> = _active.asStateFlow()

    fun addOffline(username: String): Account {
        val clean = username.trim()
        // Reuse existing offline account with same name if present
        val existing = _accounts.value.find {
            it.type == AccountType.OFFLINE && it.username.equals(clean, ignoreCase = true)
        }
        if (existing != null) {
            _active.value = existing
            return existing
        }
        val account = Account(
            id = "offline-${clean.lowercase()}-${System.currentTimeMillis()}",
            username = clean,
            type = AccountType.OFFLINE
        )
        _accounts.update { it + account }
        _active.value = account
        return account
    }

    fun setActive(account: Account?) {
        _active.value = account
    }

    fun remove(id: String) {
        _accounts.update { list -> list.filterNot { it.id == id } }
        if (_active.value?.id == id) {
            _active.value = _accounts.value.firstOrNull()
        }
    }
}

class InstanceRepository {
    private val _instances = MutableStateFlow<List<Instance>>(emptyList())
    val instances: StateFlow<List<Instance>> = _instances.asStateFlow()

    fun create(
        name: String,
        minecraftVersion: String,
        loader: LoaderType,
        loaderVersion: String? = null,
        ramMb: Int = 4096
    ): Instance {
        val instance = Instance(
            id = "inst-${System.currentTimeMillis()}",
            name = name,
            minecraftVersion = minecraftVersion,
            loader = loader,
            loaderVersion = loaderVersion,
            ramMb = ramMb
        )
        _instances.update { it + instance }
        return instance
    }

    fun delete(id: String) {
        _instances.update { list -> list.filterNot { it.id == id } }
    }
}

/** Simple app-wide holder until proper DI is introduced */
object AppState {
    val accounts = AccountRepository()
    val instances = InstanceRepository()
}
