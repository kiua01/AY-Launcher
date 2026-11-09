package com.aylauncher.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aylauncher.ui.theme.*

@Composable
fun AboutScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(40.dp))
        Text("AY Launcher", fontWeight = FontWeight.Bold, fontSize = 32.sp, color = TextPrimary)
        Text("v0.1.0-SNAPSHOT", color = TextSecondary)
        Spacer(Modifier.height(24.dp))

        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Column(Modifier.padding(24.dp)) {
                Text("Built from scratch", fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(8.dp))
                Text(
                    "Kotlin Multiplatform + Compose Multiplatform for the UI.\n" +
                    "Rust core for performance-critical work.\n" +
                    "Designed for Desktop and large Android screens (sw ≥ 1000).",
                    color = TextSecondary,
                    fontSize = 14.sp
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    "Not affiliated with Mojang, Microsoft, or any other launcher project.\n" +
                    "You need a legitimate Minecraft: Java Edition license to play online.",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            }
        }

        Spacer(Modifier.height(32.dp))
        Text("Made with ❤️ for the community", color = TextSecondary, fontSize = 13.sp)
    }
}
