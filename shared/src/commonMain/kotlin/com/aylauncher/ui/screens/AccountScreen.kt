package com.aylauncher.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aylauncher.data.AppState
import com.aylauncher.data.AuthState
import com.aylauncher.domain.Account
import com.aylauncher.domain.AccountType
import com.aylauncher.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun AccountScreen(
    onSelectOffline: () -> Unit = {},
    onSelectMicrosoft: () -> Unit = {}
) {
    val active by AppState.accounts.activeAccount.collectAsState()
    val accounts by AppState.accounts.accounts.collectAsState()
    val authState by AppState.auth.state.collectAsState()
    val scope = rememberCoroutineScope()

    var showOfflineDialog by remember { mutableStateOf(false) }
    var offlineUsername by remember { mutableStateOf("") }
    var offlineError by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF8B0000), Color(0xFFFF4500), Color(0xFF4A148C))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "AY LAUNCHER",
                color = Color.White.copy(0.9f),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "Account",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp
            )

            if (active != null) {
                Spacer(Modifier.height(8.dp))
                Text(
                    "Signed in as ${active!!.username}",
                    color = Color.White.copy(0.9f),
                    fontSize = 14.sp
                )
            } else {
                Text(
                    "Choose how you want to play",
                    color = Color.White.copy(0.85f),
                    fontSize = 15.sp
                )
            }

            Spacer(Modifier.height(24.dp))

            // Microsoft flow panel (when active)
            when (val s = authState) {
                is AuthState.WaitingForUser, is AuthState.Polling, is AuthState.Starting -> {
                    MicrosoftFlowCard(
                        state = s,
                        onSimulateComplete = {
                            scope.launch { AppState.auth.completeMicrosoftFlow() }
                        },
                        onCancel = { AppState.auth.cancel() }
                    )
                    Spacer(Modifier.height(20.dp))
                }
                is AuthState.Success -> {
                    // brief success then reset
                    LaunchedEffect(s) {
                        kotlinx.coroutines.delay(1500)
                        AppState.auth.reset()
                    }
                }
                is AuthState.Error -> {
                    Text(s.message, color = Color(0xFFFFCDD2), fontSize = 13.sp)
                    Spacer(Modifier.height(12.dp))
                    TextButton(onClick = { AppState.auth.reset() }) {
                        Text("Dismiss", color = Color.White)
                    }
                    Spacer(Modifier.height(12.dp))
                }
                else -> { /* Idle – show the two cards */ }
            }

            if (authState is AuthState.Idle || authState is AuthState.Success || authState is AuthState.Error) {
                Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                    AccountCard(
                        title = "Offline Account",
                        subtitle = "Play without signing in to Microsoft.",
                        buttonText = "Select →",
                        buttonColor = GoldButton,
                        recommended = false,
                        onClick = {
                            offlineError = null
                            offlineUsername = ""
                            showOfflineDialog = true
                        }
                    )
                    AccountCard(
                        title = "Microsoft Account",
                        subtitle = "AY Launcher recommends to play with it.",
                        buttonText = "Sign In with Microsoft →",
                        buttonColor = Color(0xFF4CAF50),
                        recommended = true,
                        onClick = {
                            onSelectMicrosoft()
                            scope.launch { AppState.auth.startMicrosoftDeviceFlow() }
                        }
                    )
                }
            }

            if (accounts.isNotEmpty()) {
                Spacer(Modifier.height(28.dp))
                Text(
                    "Saved accounts",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
                Spacer(Modifier.height(10.dp))
                accounts.forEach { acc ->
                    SavedAccountRow(
                        account = acc,
                        isActive = acc.id == active?.id,
                        onSelect = { AppState.accounts.setActive(acc) }
                    )
                }
            }
        }
    }

    if (showOfflineDialog) {
        OfflineUsernameDialog(
            username = offlineUsername,
            error = offlineError,
            onUsernameChange = {
                offlineUsername = it
                offlineError = null
            },
            onConfirm = {
                try {
                    val name = offlineUsername.trim()
                    AppState.accounts.addOffline(name)
                    offlineUsername = ""
                    offlineError = null
                    showOfflineDialog = false
                    onSelectOffline()
                } catch (e: IllegalArgumentException) {
                    offlineError = e.message
                }
            },
            onDismiss = {
                showOfflineDialog = false
                offlineError = null
            }
        )
    }
}

@Composable
private fun MicrosoftFlowCard(
    state: AuthState,
    onSimulateComplete: () -> Unit,
    onCancel: () -> Unit
) {
    val clipboard = LocalClipboardManager.current

    Card(
        modifier = Modifier.fillMaxWidth(0.85f),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Microsoft Sign-In", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(Modifier.height(8.dp))

            when (state) {
                is AuthState.Starting -> {
                    CircularProgressIndicator(color = WarmYellow)
                    Spacer(Modifier.height(12.dp))
                    Text("Starting secure login…", color = TextSecondary)
                }
                is AuthState.WaitingForUser -> {
                    Text(state.message, color = TextSecondary, fontSize = 14.sp)
                    Spacer(Modifier.height(16.dp))
                    Text(
                        state.userCode,
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                        color = TextPrimary
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(state.verificationUri, color = TextSecondary, fontSize = 13.sp)
                    Spacer(Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedButton(
                            onClick = {
                                clipboard.setText(AnnotatedString(state.userCode))
                            },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Copy code")
                        }
                        Button(
                            onClick = onSimulateComplete,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("I’ve completed it", color = Color.White)
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    TextButton(onClick = onCancel) {
                        Text("Cancel", color = TextSecondary)
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "This is a UI simulation. Real Microsoft login will be added later.",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
                is AuthState.Polling -> {
                    CircularProgressIndicator(color = WarmYellow)
                    Spacer(Modifier.height(12.dp))
                    Text("Waiting for confirmation…", color = TextSecondary)
                }
                else -> {}
            }
        }
    }
}

@Composable
private fun OfflineUsernameDialog(
    username: String,
    error: String?,
    onUsernameChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Offline Account", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                Text("Enter a username (3–16 characters).", color = TextSecondary, fontSize = 14.sp)
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = username,
                    onValueChange = onUsernameChange,
                    label = { Text("Username") },
                    singleLine = true,
                    isError = error != null,
                    supportingText = error?.let { { Text(it) } },
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { onConfirm() })
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = GoldButton),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Create", color = TextPrimary, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        shape = RoundedCornerShape(20.dp),
        containerColor = CardBackground
    )
}

@Composable
private fun SavedAccountRow(
    account: Account,
    isActive: Boolean,
    onSelect: () -> Unit
) {
    Surface(
        onClick = onSelect,
        shape = RoundedCornerShape(14.dp),
        color = if (isActive) Color.White.copy(alpha = 0.25f) else Color.White.copy(alpha = 0.12f),
        modifier = Modifier
            .fillMaxWidth(0.75f)
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (account.type == AccountType.OFFLINE) Color(0xFF78909C)
                        else Color(0xFF4CAF50)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    if (account.type == AccountType.OFFLINE) "O" else "M",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(account.username, color = Color.White, fontWeight = FontWeight.Medium)
                Text(
                    if (account.type == AccountType.OFFLINE) "Offline" else "Microsoft",
                    color = Color.White.copy(0.7f),
                    fontSize = 12.sp
                )
            }
            if (isActive) {
                Text("Active", color = GoldButton, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun AccountCard(
    title: String,
    subtitle: String,
    buttonText: String,
    buttonColor: Color,
    recommended: Boolean = false,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.width(290.dp).height(330.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(22.dp).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(SoftCream),
                contentAlignment = Alignment.Center
            ) {
                Text(if (recommended) "👑" else "🧑", fontSize = 48.sp)
            }
            Spacer(Modifier.height(12.dp))
            Text(title, fontWeight = FontWeight.Bold, fontSize = 17.sp)
            if (recommended) {
                Spacer(Modifier.height(4.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFFFF3C4)
                ) {
                    Text(
                        "⭐ Recommended",
                        color = WarmYellow,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                subtitle,
                fontSize = 13.sp,
                color = TextSecondary,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            Spacer(Modifier.weight(1f))
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                shape = RoundedCornerShape(22.dp),
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Text(
                    buttonText,
                    color = if (buttonColor == GoldButton) TextPrimary else Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
