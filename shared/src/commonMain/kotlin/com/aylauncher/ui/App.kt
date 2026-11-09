package com.aylauncher.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aylauncher.data.AppState
import com.aylauncher.domain.AccountType
import com.aylauncher.ui.screens.*
import com.aylauncher.ui.theme.*

enum class AppScreen {
    Home, Installations, Skins, Marketplace, Settings, About, FileManager, Account
}

@Composable
fun AYLauncherApp() {
    var currentScreen by remember { mutableStateOf(AppScreen.Home) }
    val activeAccount by AppState.accounts.activeAccount.collectAsState()

    AYLauncherTheme {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(CreamBackground)
        ) {
            Sidebar(
                current = currentScreen,
                activeUsername = activeAccount?.username,
                onNavigate = { currentScreen = it }
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(16.dp)
            ) {
                when (currentScreen) {
                    AppScreen.Home -> HomeScreen(
                        activeUsername = activeAccount?.username,
                        onPlay = { currentScreen = AppScreen.Installations },
                        onOpenAccount = { currentScreen = AppScreen.Account }
                    )
                    AppScreen.Installations -> InstallationsScreen()
                    AppScreen.Skins -> PlaceholderScreen("Skins")
                    AppScreen.Marketplace -> PlaceholderScreen("Marketplace")
                    AppScreen.Settings -> SettingsScreen()
                    AppScreen.About -> AboutScreen()
                    AppScreen.FileManager -> FileManagerScreen()
                    AppScreen.Account -> AccountScreen(
                        onSelectOffline = { /* already handled inside */ },
                        onSelectMicrosoft = {
                            // Placeholder: real OAuth later
                            // For now just show a message via dialog or snackbar later
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun Sidebar(
    current: AppScreen,
    activeUsername: String?,
    onNavigate: (AppScreen) -> Unit
) {
    Column(
        modifier = Modifier
            .width(96.dp)
            .fillMaxHeight()
            .background(SoftCream)
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(CreeperGreen)
                .clickable { onNavigate(AppScreen.Home) },
            contentAlignment = Alignment.Center
        ) {
            Text("AY", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        Spacer(Modifier.height(20.dp))

        SidebarItem("🏠", "Home", current == AppScreen.Home) { onNavigate(AppScreen.Home) }
        SidebarItem("📦", "Installations", current == AppScreen.Installations) { onNavigate(AppScreen.Installations) }
        SidebarItem("👕", "Skins", current == AppScreen.Skins) { onNavigate(AppScreen.Skins) }
        SidebarItem("🛒", "Marketplace", current == AppScreen.Marketplace) { onNavigate(AppScreen.Marketplace) }
        SidebarItem("⚙️", "Settings", current == AppScreen.Settings) { onNavigate(AppScreen.Settings) }
        SidebarItem("ℹ️", "About", current == AppScreen.About) { onNavigate(AppScreen.About) }
        SidebarItem("📁", "File Manager", current == AppScreen.FileManager) { onNavigate(AppScreen.FileManager) }
        SidebarItem("👤", "Account", current == AppScreen.Account) { onNavigate(AppScreen.Account) }

        Spacer(Modifier.weight(1f))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("● Online", color = OnlineGreen, fontSize = 11.sp, fontWeight = FontWeight.Medium)
            Text(
                activeUsername ?: "No account",
                color = TextPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
            Text("AY Launcher", color = TextSecondary, fontSize = 10.sp)
            Text("v0.1.0", color = TextSecondary, fontSize = 10.sp)
        }
    }
}

@Composable
fun SidebarItem(
    icon: String,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val bg = if (selected) SidebarSelected else Color.Transparent
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp, vertical = 3.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(bg)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                role = Role.Button,
                onClick = onClick
            )
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(icon, fontSize = 22.sp)
        Text(
            text = label,
            fontSize = 10.sp,
            color = if (selected) TextPrimary else TextSecondary,
            maxLines = 1,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}
