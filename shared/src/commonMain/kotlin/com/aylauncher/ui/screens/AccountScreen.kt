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
fun AccountScreen(
    onSelectOffline: () -> Unit = {},
    onSelectMicrosoft: () -> Unit = {}
) {
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
                fontSize = 16.sp
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "Account Type",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 34.sp
            )
            Text(
                "Choose how you want to play",
                color = Color.White.copy(0.85f),
                fontSize = 16.sp
            )

            Spacer(Modifier.height(36.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                AccountCard(
                    title = "Offline Account",
                    subtitle = "Play without signing in to Microsoft.",
                    buttonText = "Select →",
                    buttonColor = GoldButton,
                    recommended = false,
                    onClick = onSelectOffline
                )
                AccountCard(
                    title = "Microsoft Account",
                    subtitle = "AY Launcher recommends to play with it.",
                    buttonText = "Sign In with Microsoft →",
                    buttonColor = Color(0xFF4CAF50),
                    recommended = true,
                    onClick = onSelectMicrosoft
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
        modifier = Modifier.width(320.dp).height(360.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(SoftCream),
                contentAlignment = Alignment.Center
            ) {
                Text(if (recommended) "👑" else "🧑", fontSize = 56.sp)
            }
            Spacer(Modifier.height(16.dp))
            Text(title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
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
            Spacer(Modifier.height(10.dp))
            Text(
                subtitle,
                fontSize = 14.sp,
                color = TextSecondary,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Spacer(Modifier.weight(1f))
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth().height(50.dp)
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
