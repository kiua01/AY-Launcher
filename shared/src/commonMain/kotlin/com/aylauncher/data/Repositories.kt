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
 * Later these will be backed by local storage + network
 * and the Rust core for heavy file / download work.
 *
 * No secrets or tokens are persisted in this stage.
 */

class AccountRepository {
    private val _accounts = MutableStateFlow<List<Account>>(emptyList())
    val accounts: StateFlow<List<Account>> = _accounts.asStateFlow()

    private val _active = MutableStateFlow<Account?>(null)
    val activeAccount: StateFlow<Account?> = _active.asStateFlow()

    fun addOffline(username: String): Account {
        val clean = username.trim()
        require(clean.isNotEmpty()) { "Username cannot be empty" }
        require(clean.length in 3..16) { "Username must be 3–16 characters" }

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

    fun addMicrosoft(account: Account): Account {
        require(account.type == AccountType.MICROSOFT)
        val existing = _accounts.value.find {
            it.type == AccountType.MICROSOFT && it.username.equals(account.username, ignoreCase = true)
        }
        if (existing != null) {
            _active.value = existing
            return existing
        }
        // Never keep a real access token in the in-memory model at this stage
        val safe = account.copy(accessToken = null)
        _accounts.update { it + safe }
        _active.value = safe
        return safe
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
        val cleanName = name.trim().ifEmpty { "New Instance" }
        val safeRam = ramMb.coerceIn(1024, 32768)
        val instance = Instance(
            id = "inst-${System.currentTimeMillis()}",
            name = cleanName,
            minecraftVersion = minecraftVersion,
            loader = loader,
            loaderVersion = loaderVersion,
            ramMb = safeRam
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
    val auth = AuthService()
}
