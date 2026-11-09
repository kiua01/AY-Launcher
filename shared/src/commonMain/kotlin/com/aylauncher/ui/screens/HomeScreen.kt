package com.aylauncher.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
fun HomeScreen(
    onPlay: () -> Unit = {},
    onOpenAccount: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Top bar
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "AY LAUNCHER",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = TextPrimary
            )
            Spacer(Modifier.weight(1f))
            // Search
            Box(
                modifier = Modifier
                    .width(280.dp)
                    .height(40.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text("🔍  Search for anything...", color = TextSecondary, fontSize = 14.sp)
            }
            Spacer(Modifier.width(12.dp))
            // User chip
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = SoftCream,
                onClick = onOpenAccount
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(CreeperGreen)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Player", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }
            }
        }

        // Hero banner – "The End awaits..."
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(270.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFF8B0000), Color(0xFFFF4500), Color(0xFF4A148C))
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(32.dp)
            ) {
                Text(
                    "AY LAUNCHER  •  JAVA EDITION",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    "The End\nawaits...",
                    color = Color.White,
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 46.sp
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Face the Nether, explore new\nchallenges and discover more!",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 14.sp
                )
                Spacer(Modifier.height(18.dp))
                Button(
                    onClick = onPlay,
                    colors = ButtonDefaults.buttonColors(containerColor = GoldButton),
                    shape = RoundedCornerShape(28.dp),
                    modifier = Modifier.height(50.dp)
                ) {
                    Text("▶  Play Now", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }

        Spacer(Modifier.height(22.dp))

        // Featured News
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Featured News", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextPrimary)
            Spacer(Modifier.weight(1f))
            TextButton(onClick = {}) {
                Text("View all →", color = TextSecondary)
            }
        }
        Spacer(Modifier.height(10.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            item { NewsCard("Spring to Life", "A fresh new look for Minecraft!", "Mar 25, 2025") }
            item { NewsCard("The Tricky Trials Update", "Take on new challenges!", "Jun 13, 2024") }
            item { NewsCard("Camel! Ride in Style", "Explore the desert with camels.", "Apr 3, 2024") }
        }

        Spacer(Modifier.height(24.dp))

        // Bottom cards
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            // What's New
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(Modifier.padding(18.dp)) {
                    Text("✨ What's New?", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(Modifier.height(10.dp))
                    Text("The Nether Update", fontWeight = FontWeight.SemiBold)
                    Text("New biomes, new features!", fontSize = 13.sp, color = TextSecondary)
                    Spacer(Modifier.height(6.dp))
                    Text("Jun 30, 2024", fontSize = 12.sp, color = TextSecondary)
                }
            }

            // Quick Links
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(Modifier.padding(18.dp)) {
                    Text("🔗 Quick Links", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        QuickChip("Official Website")
                        QuickChip("Patch Notes")
                        QuickChip("Discord")
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        QuickChip("Marketplace")
                        QuickChip("Skins")
                        QuickChip("Gift Codes")
                    }
                }
            }

            // Join Realms
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(Modifier.padding(18.dp)) {
                    Text("👑 Join Realms", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(Modifier.height(8.dp))
                    Text("Play with friends anywhere, anytime!", fontSize = 13.sp, color = TextSecondary)
                    Spacer(Modifier.height(14.dp))
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E2E2E)),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Learn More →", color = Color.White, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
fun NewsCard(title: String, subtitle: String, date: String) {
    Card(
        modifier = Modifier.width(230.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(115.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFFFFCC80), Color(0xFFFFAB40))
                        )
                    )
            )
            Column(Modifier.padding(14.dp)) {
                Text(title, fontWeight = FontWeight.SemiBold, maxLines = 1, fontSize = 14.sp)
                Text(subtitle, fontSize = 12.sp, color = TextSecondary, maxLines = 2)
                Spacer(Modifier.height(4.dp))
                Text(date, fontSize = 11.sp, color = TextSecondary)
            }
        }
    }
}

@Composable
fun QuickChip(text: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = SoftCream
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            fontSize = 12.sp
        )
    }
}
