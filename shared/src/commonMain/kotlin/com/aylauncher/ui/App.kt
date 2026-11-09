package com.aylauncher.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aylauncher.ui.screens.*
import com.aylauncher.ui.theme.*

enum class AppScreen {
    Home, Installations, Skins, Marketplace, Settings, About, FileManager, Account
}

@Composable
fun AYLauncherApp() {
    var currentScreen by remember { mutableStateOf(AppScreen.Home) }

    AYLauncherTheme {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(CreamBackground)
        ) {
            // Left Sidebar – matches the design
            Sidebar(
                current = currentScreen,
                onNavigate = { currentScreen = it }
            )

            // Main content area
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(16.dp)
            ) {
                when (currentScreen) {
                    AppScreen.Home -> HomeScreen()
                    AppScreen.Installations -> InstallationsScreen()
                    AppScreen.Skins -> PlaceholderScreen("Skins")
                    AppScreen.Marketplace -> PlaceholderScreen("Marketplace")
                    AppScreen.Settings -> SettingsScreen()
                    AppScreen.About -> PlaceholderScreen("About")
                    AppScreen.FileManager -> FileManagerScreen()
                    AppScreen.Account -> AccountScreen()
                }
            }
        }
    }
}

@Composable
fun Sidebar(
    current: AppScreen,
    onNavigate: (AppScreen) -> Unit
) {
    Column(
        modifier = Modifier
            .width(92.dp)
            .fillMaxHeight()
            .background(SoftCream)
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Creeper / logo area
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(CreeperGreen),
            contentAlignment = Alignment.Center
        ) {
            Text("AY", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        Spacer(Modifier.height(24.dp))

        SidebarItem("🏠", "Home", current == AppScreen.Home) { onNavigate(AppScreen.Home) }
        SidebarItem("📦", "Installations", current == AppScreen.Installations) { onNavigate(AppScreen.Installations) }
        SidebarItem("👕", "Skins", current == AppScreen.Skins) { onNavigate(AppScreen.Skins) }
        SidebarItem("🛒", "Marketplace", current == AppScreen.Marketplace) { onNavigate(AppScreen.Marketplace) }
        SidebarItem("⚙️", "Settings", current == AppScreen.Settings) { onNavigate(AppScreen.Settings) }
        SidebarItem("ℹ️", "About", current == AppScreen.About) { onNavigate(AppScreen.About) }
        SidebarItem("📁", "File Manager", current == AppScreen.FileManager) { onNavigate(AppScreen.FileManager) }
        SidebarItem("👤", "Account", current == AppScreen.Account) { onNavigate(AppScreen.Account) }

        Spacer(Modifier.weight(1f))

        // Online indicator
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("● Online", color = OnlineGreen, fontSize = 11.sp)
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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(bg)
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(icon, fontSize = 22.sp)
        Text(label, fontSize = 10.sp, color = TextPrimary, maxLines = 1)
        // We use a simple clickable via the whole column – for production use Modifier.clickable
        // (added in full implementation)
    }
}
