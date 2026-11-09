package com.aylauncher.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aylauncher.ui.theme.*

@Composable
fun HomeScreen(
    activeUsername: String? = null,
    onPlay: () -> Unit = {},
    onOpenAccount: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        TopBar(
            username = activeUsername ?: "CreeperFan",
            onOpenAccount = onOpenAccount
        )

        Spacer(Modifier.height(10.dp))

        // Main row: hero+news | right column
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // LEFT / CENTER column
            Column(modifier = Modifier.weight(1.55f)) {
                HeroBanner(onPlay = onPlay)
                Spacer(Modifier.height(16.dp))
                FeaturedNewsSection()
            }

            // RIGHT column
            Column(
                modifier = Modifier
                    .weight(0.85f)
                    .widthIn(max = 320.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                WhatsNewCard()
                QuickLinksCard()
                JoinRealmsCard()
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun TopBar(username: String, onOpenAccount: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Minecraft Java Edition wordmark
        Column {
            Text(
                "MINECRAFT",
                fontWeight = FontWeight.Black,
                fontSize = 18.sp,
                color = TextPrimary,
                letterSpacing = 1.sp
            )
            Text(
                "JAVA EDITION",
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = TextSecondary,
                letterSpacing = 0.5.sp
            )
        }

        Spacer(Modifier.width(20.dp))

        // Search
        Box(
            modifier = Modifier
                .weight(1f)
                .height(42.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .border(1.dp, Color(0xFFFFE0B2), RoundedCornerShape(24.dp))
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text("🔍   Search for anything...", color = TextSecondary.copy(alpha = 0.7f), fontSize = 14.sp)
        }

        Spacer(Modifier.width(12.dp))

        // Notification bell with badge
        Box {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text("🔔", fontSize = 16.sp)
            }
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 2.dp, y = (-2).dp)
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(BadgeRed),
                contentAlignment = Alignment.Center
            ) {
                Text("3", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(Modifier.width(8.dp))

        // Controller
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Text("🎮", fontSize = 16.sp)
        }

        Spacer(Modifier.width(10.dp))

        // User chip
        Surface(
            shape = RoundedCornerShape(22.dp),
            color = Color.White,
            onClick = onOpenAccount,
            shadowElevation = 1.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(CreeperFaceGreen)
                )
                Spacer(Modifier.width(8.dp))
                Text(username, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                Spacer(Modifier.width(4.dp))
                Text("▾", fontSize = 11.sp, color = TextSecondary)
            }
        }
    }
}

@Composable
private fun HeroBanner(onPlay: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        Color(0xFF5C0000),
                        Color(0xFFB71C1C),
                        Color(0xFFFF6D00),
                        Color(0xFF4A148C)
                    )
                )
            )
    ) {
        // Soft vignette overlays to suggest scene depth
        Box(
            Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, Color.Black.copy(alpha = 0.25f))
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 28.dp, end = 28.dp, top = 24.dp, bottom = 24.dp)
        ) {
            Text(
                "MINECRAFT",
                color = Color.White,
                fontWeight = FontWeight.Black,
                fontSize = 15.sp,
                letterSpacing = 2.sp
            )
            Text(
                "JAVA EDITION",
                color = Color.White.copy(0.9f),
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                letterSpacing = 1.sp
            )
            Spacer(Modifier.height(10.dp))
            Text(
                buildAnnotatedString {
                    withStyle(SpanStyle(color = Color.White, fontWeight = FontWeight.Bold)) {
                        append("The End\n")
                    }
                    withStyle(SpanStyle(color = Color(0xFFFFEB3B), fontWeight = FontWeight.Bold)) {
                        append("awaits...")
                    }
                },
                fontSize = 40.sp,
                lineHeight = 44.sp
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Face the Nether, explore new\nchallenges and discover more!",
                color = Color.White.copy(0.88f),
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
            Spacer(Modifier.height(18.dp))
            Button(
                onClick = onPlay,
                colors = ButtonDefaults.buttonColors(containerColor = PlayButtonYellow),
                shape = RoundedCornerShape(28.dp),
                contentPadding = PaddingValues(horizontal = 28.dp, vertical = 14.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text("▶", fontSize = 16.sp, color = TextPrimary)
                Spacer(Modifier.width(8.dp))
                Text("Play Now", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }

        // Carousel dots
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(Modifier.size(7.dp).clip(CircleShape).background(Color.White))
            repeat(4) {
                Box(Modifier.size(7.dp).clip(CircleShape).background(Color.White.copy(0.4f)))
            }
        }
    }
}

@Composable
private fun FeaturedNewsSection() {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("📰", fontSize = 16.sp)
            Spacer(Modifier.width(6.dp))
            Text("Featured News", fontWeight = FontWeight.Bold, fontSize = 17.sp, color = TextPrimary)
            Spacer(Modifier.weight(1f))
            Text(
                "View all →",
                color = TextSecondary,
                fontSize = 13.sp,
                modifier = Modifier.clickable { }
            )
        }
        Spacer(Modifier.height(12.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            item {
                NewsCard(
                    title = "Spring to Life",
                    subtitle = "A fresh new look for Minecraft!",
                    date = "Mar 25, 2025",
                    gradient = listOf(Color(0xFFF8BBD0), Color(0xFFFFCC80), Color(0xFFA5D6A7))
                )
            }
            item {
                NewsCard(
                    title = "The Tricky Trials Update",
                    subtitle = "Take on new challenges!",
                    date = "Jun 13, 2024",
                    gradient = listOf(Color(0xFF4FC3F7), Color(0xFFFF8A65), Color(0xFF5C6BC0))
                )
            }
            item {
                NewsCard(
                    title = "Camel! Ride in Style",
                    subtitle = "Explore the desert with camels.",
                    date = "Apr 3, 2024",
                    gradient = listOf(Color(0xFFFFE082), Color(0xFFFFCC80), Color(0xFFBCAAA4))
                )
            }
        }
    }
}

@Composable
private fun NewsCard(
    title: String,
    subtitle: String,
    date: String,
    gradient: List<Color>
) {
    Card(
        modifier = Modifier.width(210.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(108.dp)
                    .background(Brush.linearGradient(gradient))
            ) {
                // News pill
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFC8E6C9),
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text(
                        "News",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF2E7D32)
                    )
                }
            }
            Column(Modifier.padding(12.dp)) {
                Text(title, fontWeight = FontWeight.SemiBold, maxLines = 1, fontSize = 14.sp, color = TextPrimary)
                Text(subtitle, fontSize = 12.sp, color = TextSecondary, maxLines = 2, lineHeight = 16.sp)
                Spacer(Modifier.height(6.dp))
                Text("📅  $date", fontSize = 11.sp, color = TextSecondary)
            }
        }
    }
}

@Composable
private fun WhatsNewCard() {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("✨ What's New?", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Spacer(Modifier.weight(1f))
                Text("View all →", fontSize = 12.sp, color = TextSecondary)
            }
            Spacer(Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFFB71C1C), Color(0xFFFF6D00), Color(0xFF6A1B9A))
                        )
                    )
            )
            Spacer(Modifier.height(10.dp))
            Text("The Nether Update", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Text("New biomes, new features!", fontSize = 12.sp, color = TextSecondary)
            Spacer(Modifier.height(4.dp))
            Text("📅  Jun 30, 2024", fontSize = 11.sp, color = TextSecondary)
        }
    }
}

@Composable
private fun QuickLinksCard() {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.padding(14.dp)) {
            Text("🔗 Quick Links", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                QuickLinkTile("🌐", "Official\nWebsite", Modifier.weight(1f))
                QuickLinkTile("📄", "Patch\nNotes", Modifier.weight(1f))
                QuickLinkTile("💬", "Discord", Modifier.weight(1f))
            }
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                QuickLinkTile("🛒", "Marketplace", Modifier.weight(1f))
                QuickLinkTile("👕", "Skins", Modifier.weight(1f))
                QuickLinkTile("🎁", "Gift\nCodes", Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun QuickLinkTile(icon: String, label: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.height(64.dp),
        shape = RoundedCornerShape(12.dp),
        color = SoftCream
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(icon, fontSize = 16.sp)
            Text(label, fontSize = 10.sp, color = TextPrimary, lineHeight = 12.sp)
        }
    }
}

@Composable
private fun JoinRealmsCard() {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.padding(14.dp)) {
            Text("👑 Join Realms", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Spacer(Modifier.height(8.dp))
            Text(
                "Play with friends anywhere, anytime!",
                fontSize = 13.sp,
                color = TextSecondary
            )
            Spacer(Modifier.height(12.dp))
            // Floating island preview
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF81D4FA), Color(0xFFA5D6A7), Color(0xFF8D6E63))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("🏝️🏠", fontSize = 28.sp)
            }
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C2C)),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Learn More →", color = Color.White, fontWeight = FontWeight.Medium)
            }
        }
    }
}
