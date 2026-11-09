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
                    AppScreen.Home -> HomeScreen(
                        onPlay = { currentScreen = AppScreen.Installations },
                        onOpenAccount = { currentScreen = AppScreen.Account }
                    )
                    AppScreen.Installations -> InstallationsScreen(
                        onCreateNew = { /* will open dialog later */ }
                    )
                    AppScreen.Skins -> PlaceholderScreen("Skins")
                    AppScreen.Marketplace -> PlaceholderScreen("Marketplace")
                    AppScreen.Settings -> SettingsScreen()
                    AppScreen.About -> AboutScreen()
                    AppScreen.FileManager -> FileManagerScreen()
                    AppScreen.Account -> AccountScreen(
                        onSelectOffline = { /* handle offline */ },
                        onSelectMicrosoft = { /* handle microsoft */ }
                    )
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
            .width(96.dp)
            .fillMaxHeight()
            .background(SoftCream)
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo / Creeper-style mark
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

        SidebarItem(
            icon = "🏠",
            label = "Home",
            selected = current == AppScreen.Home,
            onClick = { onNavigate(AppScreen.Home) }
        )
        SidebarItem(
            icon = "📦",
            label = "Installations",
            selected = current == AppScreen.Installations,
            onClick = { onNavigate(AppScreen.Installations) }
        )
        SidebarItem(
            icon = "👕",
            label = "Skins",
            selected = current == AppScreen.Skins,
            onClick = { onNavigate(AppScreen.Skins) }
        )
        SidebarItem(
            icon = "🛒",
            label = "Marketplace",
            selected = current == AppScreen.Marketplace,
            onClick = { onNavigate(AppScreen.Marketplace) }
        )
        SidebarItem(
            icon = "⚙️",
            label = "Settings",
            selected = current == AppScreen.Settings,
            onClick = { onNavigate(AppScreen.Settings) }
        )
        SidebarItem(
            icon = "ℹ️",
            label = "About",
            selected = current == AppScreen.About,
            onClick = { onNavigate(AppScreen.About) }
        )
        SidebarItem(
            icon = "📁",
            label = "File Manager",
            selected = current == AppScreen.FileManager,
            onClick = { onNavigate(AppScreen.FileManager) }
        )
        SidebarItem(
            icon = "👤",
            label = "Account",
            selected = current == AppScreen.Account,
            onClick = { onNavigate(AppScreen.Account) }
        )

        Spacer(Modifier.weight(1f))

        // Online indicator
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("● Online", color = OnlineGreen, fontSize = 11.sp, fontWeight = FontWeight.Medium)
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
                indication = null, // keep clean look matching designs
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
