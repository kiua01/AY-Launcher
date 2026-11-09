package com.aylauncher.data

import com.aylauncher.domain.Account
import com.aylauncher.domain.AccountType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Microsoft authentication service (stub only).
 *
 * Real implementation will use the official Microsoft OAuth 2.0 device code flow:
 * 1. Request device code from login.microsoftonline.com
 * 2. Show user_code + verification_uri to the user
 * 3. Poll for token
 * 4. Exchange for Xbox Live → XSTS → Minecraft services token
 *
 * This file deliberately contains NO client secrets and performs NO real network
 * authentication. It only drives the UI so the screens can be finished safely.
 */
sealed class AuthState {
    data object Idle : AuthState()
    data object Starting : AuthState()
    data class WaitingForUser(
        val userCode: String,
        val verificationUri: String,
        val message: String
    ) : AuthState()
    data object Polling : AuthState()
    data class Success(val account: Account) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthService {
    private val _state = MutableStateFlow<AuthState>(AuthState.Idle)
    val state: StateFlow<AuthState> = _state.asStateFlow()

    /** Starts a simulated device-code flow (UI only). */
    suspend fun startMicrosoftDeviceFlow() {
        _state.value = AuthState.Starting
        delay(500)

        _state.value = AuthState.WaitingForUser(
            userCode = "AYLA-7K2P",
            verificationUri = "https://www.microsoft.com/link",
            message = "Go to the link on any device and enter the code."
        )
    }

    /**
     * Simulates the user completing the browser step.
     * In production this would be the result of the polling loop.
     */
    suspend fun completeMicrosoftFlow(username: String = "MicrosoftPlayer") {
        val current = _state.value
        if (current !is AuthState.WaitingForUser && current !is AuthState.Polling) {
            _state.value = AuthState.Error("No login in progress")
            return
        }
        _state.value = AuthState.Polling
        delay(1000)

        val account = Account(
            id = "ms-${System.currentTimeMillis()}",
            username = username.trim().ifEmpty { "MicrosoftPlayer" },
            type = AccountType.MICROSOFT,
            uuid = null,
            accessToken = null // never store real tokens in this stub
        )
        AppState.accounts.addMicrosoft(account)
        _state.value = AuthState.Success(account)
    }

    fun cancel() {
        _state.value = AuthState.Idle
    }

    fun reset() {
        _state.value = AuthState.Idle
    }
}
