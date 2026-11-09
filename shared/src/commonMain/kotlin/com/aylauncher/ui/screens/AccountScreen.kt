package com.aylauncher.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aylauncher.ui.theme.*

@Composable
fun AccountScreen() {
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
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "AY LAUNCHER",
                color = Color.White.copy(0.9f),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Account Type",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp
            )
            Text(
                "Choose how you want to play",
                color = Color.White.copy(0.85f),
                fontSize = 16.sp
            )

            Spacer(Modifier.height(40.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Offline
                AccountCard(
                    title = "Offline Account",
                    subtitle = "Play without signing in to Microsoft.",
                    buttonText = "Select →",
                    buttonColor = GoldButton,
                    onClick = {}
                )
                // Microsoft
                AccountCard(
                    title = "Microsoft Account",
                    subtitle = "AY Launcher recommends to play with it.",
                    buttonText = "Sign In with Microsoft →",
                    buttonColor = Color(0xFF4CAF50),
                    recommended = true,
                    onClick = {}
                )
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
        modifier = Modifier.width(320.dp).height(340.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground)
    ) {
        Column(
            modifier = Modifier.padding(24.dp).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Placeholder avatar area
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(SoftCream),
                contentAlignment = Alignment.Center
            ) {
                Text(if (recommended) "👑" else "🧑", fontSize = 48.sp)
            }
            Spacer(Modifier.height(16.dp))
            Text(title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            if (recommended) {
                Text("⭐ Recommended", color = WarmYellow, fontSize = 13.sp)
            }
            Spacer(Modifier.height(8.dp))
            Text(subtitle, fontSize = 14.sp, color = TextSecondary, modifier = Modifier.padding(horizontal = 8.dp))
            Spacer(Modifier.weight(1f))
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Text(buttonText, color = if (buttonColor == GoldButton) TextPrimary else Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}
