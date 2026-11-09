package com.aylauncher.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aylauncher.ui.theme.*

data class SettingsCategory(val title: String, val description: String, val icon: String)

@Composable
fun SettingsScreen() {
    val categories = listOf(
        SettingsCategory("General", "App language, theme, and more.", "🖥️"),
        SettingsCategory("Minecraft", "Game settings and launch options.", "🟩"),
        SettingsCategory("Performance", "Allocate more resources for smoother gameplay.", "⚡"),
        SettingsCategory("Downloads", "Manage downloads and cache.", "⬇️"),
        SettingsCategory("Launcher Appearance", "Theme, colors, and interface style.", "🎨"),
        SettingsCategory("Miscellaneous Settings", "Various settings like UI, updates and more.", "🔧"),
        SettingsCategory("Experimental Stuff", "Try experimental features and unsupported options.", "🧪"),
        SettingsCategory("Control Customization", "Customize keybinds, touch controls and layouts.", "🎮"),
    )

    Column(Modifier.fillMaxSize()) {
        // Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .background(
                        Brush.horizontalGradient(listOf(Color(0xFF8B0000), Color(0xFFFF4500)))
                    )
                    .padding(24.dp)
            ) {
                Column {
                    Text("⚙️ Settings", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 26.sp)
                    Text("Customize AY Launcher Plus to match your style.", color = Color.White.copy(0.9f))
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(categories) { cat ->
                SettingsCard(cat)
            }
        }
    }
}

@Composable
fun SettingsCard(cat: SettingsCategory) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        modifier = Modifier.height(110.dp)
    ) {
        Row(Modifier.padding(16.dp).fillMaxSize()) {
            Text(cat.icon, fontSize = 28.sp)
            Spacer(Modifier.width(12.dp))
            Column {
                Text(cat.title, fontWeight = FontWeight.Bold)
                Text(cat.description, fontSize = 13.sp, color = TextSecondary, maxLines = 2)
            }
        }
    }
}
