package com.aylauncher.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import com.aylauncher.resources.Res
import com.aylauncher.resources.creeper_face
import com.aylauncher.ui.screens.*
import com.aylauncher.ui.theme.*
import org.jetbrains.compose.resources.painterResource

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
                activeUsername = activeAccount?.username ?: "CreeperFan",
                onNavigate = { currentScreen = it }
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                when (currentScreen) {
                    AppScreen.Home -> HomeScreen(
                        activeUsername = activeAccount?.username ?: "CreeperFan",
                        onPlay = { currentScreen = AppScreen.Installations },
                        onOpenAccount = { currentScreen = AppScreen.Account }
                    )
                    AppScreen.Installations -> InstallationsScreen()
                    AppScreen.Skins -> PlaceholderScreen("Skins")
                    AppScreen.Marketplace -> PlaceholderScreen("Marketplace")
                    AppScreen.Settings -> SettingsScreen()
                    AppScreen.About -> AboutScreen()
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
    activeUsername: String,
    onNavigate: (AppScreen) -> Unit
) {
    Column(
        modifier = Modifier
            .width(88.dp)
            .fillMaxHeight()
            .background(SidebarBg)
            .padding(vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Creeper face logo (reference asset)
        Image(
            painter = painterResource(Res.drawable.creeper_face),
            contentDescription = "AY Launcher",
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable { onNavigate(AppScreen.Home) },
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.height(18.dp))

        SidebarItem("⌂", "Home", current == AppScreen.Home) { onNavigate(AppScreen.Home) }
        SidebarItem("▦", "Installations", current == AppScreen.Installations) { onNavigate(AppScreen.Installations) }
        SidebarItem("👕", "Skins", current == AppScreen.Skins) { onNavigate(AppScreen.Skins) }
        SidebarItem("💼", "Marketplace", current == AppScreen.Marketplace) { onNavigate(AppScreen.Marketplace) }
        SidebarItem("⚙", "Settings", current == AppScreen.Settings) { onNavigate(AppScreen.Settings) }
        SidebarItem("ℹ", "About", current == AppScreen.About) { onNavigate(AppScreen.About) }

        Spacer(Modifier.weight(1f))

        // Bottom user status matching screenshot
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 6.dp)
        ) {
            Image(
                painter = painterResource(Res.drawable.creeper_face),
                contentDescription = null,
                modifier = Modifier
                    .size(28.dp)
                    .clip(RoundedCornerShape(6.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.height(4.dp))
            Text(
                activeUsername.take(12),
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                maxLines = 1
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(OnlineGreen)
                )
                Spacer(Modifier.width(3.dp))
                Text("Online", fontSize = 9.sp, color = OnlineGreen)
            }
            Spacer(Modifier.height(4.dp))
            Text("Latest Release", fontSize = 8.sp, color = TextSecondary)
            Text("1.21.10", fontSize = 9.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
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
            .padding(horizontal = 8.dp, vertical = 3.dp)
            .clip(RoundedCornerShape(14.dp))
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
        Text(icon, fontSize = 18.sp, color = if (selected) TextPrimary else TextSecondary)
        Spacer(Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 9.sp,
            color = if (selected) TextPrimary else TextSecondary,
            maxLines = 1,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}
