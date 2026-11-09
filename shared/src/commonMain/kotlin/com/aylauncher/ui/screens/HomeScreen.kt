package com.aylauncher.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aylauncher.resources.Res
import com.aylauncher.resources.creeper_face
import com.aylauncher.resources.hero_nether
import com.aylauncher.resources.news_camel
import com.aylauncher.resources.news_spring
import com.aylauncher.resources.news_trials
import com.aylauncher.resources.realms_island
import com.aylauncher.ui.theme.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun HomeScreen(
    activeUsername: String? = null,
    onPlay: () -> Unit = {},
    onOpenAccount: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBackground)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 12.dp)
    ) {
        TopBar(
            username = activeUsername ?: "CreeperFan",
            onOpenAccount = onOpenAccount
        )

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Column(modifier = Modifier.weight(1.65f)) {
                HeroBanner(onPlay = onPlay)
                Spacer(Modifier.height(18.dp))
                FeaturedNewsSection()
            }

            Column(
                modifier = Modifier
                    .weight(0.9f)
                    .widthIn(min = 260.dp, max = 340.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                WhatsNewCard()
                QuickLinksCard()
                JoinRealmsCard()
            }
        }
    }
}

@Composable
private fun TopBar(username: String, onOpenAccount: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.padding(end = 12.dp)) {
            Text(
                "MINECRAFT",
                fontWeight = FontWeight.Black,
                fontSize = 17.sp,
                color = TextPrimary,
                letterSpacing = 0.8.sp
            )
            Text(
                "JAVA EDITION",
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                color = TextSecondary,
                letterSpacing = 1.2.sp
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .height(40.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(Color.White)
                .border(1.dp, Color(0xFFFFE4C4), RoundedCornerShape(22.dp))
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                "Search for anything...",
                color = TextSecondary.copy(alpha = 0.65f),
                fontSize = 14.sp
            )
        }

        Spacer(Modifier.width(10.dp))

        Box {
            RoundIconButton("🔔")
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 3.dp, y = (-3).dp)
                    .size(15.dp)
                    .clip(CircleShape)
                    .background(BadgeRed),
                contentAlignment = Alignment.Center
            ) {
                Text("3", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(Modifier.width(6.dp))
        RoundIconButton("🎮")
        Spacer(Modifier.width(8.dp))

        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            onClick = onOpenAccount,
            shadowElevation = 1.dp
        ) {
            Row(
                modifier = Modifier.padding(start = 6.dp, end = 10.dp, top = 5.dp, bottom = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(Res.drawable.creeper_face),
                    contentDescription = null,
                    modifier = Modifier
                        .size(26.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.width(8.dp))
                Text(username, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                Spacer(Modifier.width(2.dp))
                Text("▾", fontSize = 11.sp, color = TextSecondary)
            }
        }
    }
}

@Composable
private fun RoundIconButton(emoji: String) {
    Box(
        modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Text(emoji, fontSize = 15.sp)
    }
}

@Composable
private fun HeroBanner(onPlay: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(310.dp)
            .clip(RoundedCornerShape(20.dp))
    ) {
        Image(
            painter = painterResource(Res.drawable.hero_nether),
            contentDescription = "Nether banner",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center
        )

        // Darken left side so text stays readable like the reference
        Box(
            Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        0f to Color.Black.copy(alpha = 0.55f),
                        0.45f to Color.Black.copy(alpha = 0.25f),
                        0.75f to Color.Transparent
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 28.dp, end = 20.dp, top = 22.dp, bottom = 22.dp)
                .widthIn(max = 340.dp)
        ) {
            Text(
                "MINECRAFT",
                color = Color.White,
                fontWeight = FontWeight.Black,
                fontSize = 14.sp,
                letterSpacing = 2.sp
            )
            Text(
                "JAVA EDITION",
                color = Color.White.copy(0.92f),
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                letterSpacing = 1.5.sp
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
                fontSize = 42.sp,
                lineHeight = 46.sp
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Face the Nether, explore new\nchallenges and discover more!",
                color = Color.White.copy(0.9f),
                fontSize = 14.sp,
                lineHeight = 19.sp
            )
            Spacer(Modifier.height(18.dp))
            Button(
                onClick = onPlay,
                colors = ButtonDefaults.buttonColors(containerColor = PlayButtonYellow),
                shape = RoundedCornerShape(26.dp),
                contentPadding = PaddingValues(horizontal = 26.dp, vertical = 12.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
            ) {
                Text("▶  Play Now", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(Modifier.size(7.dp).clip(CircleShape).background(Color.White))
            repeat(4) {
                Box(Modifier.size(7.dp).clip(CircleShape).background(Color.White.copy(0.45f)))
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
            Text("Featured News", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextPrimary)
            Spacer(Modifier.weight(1f))
            Text("View all →", color = TextSecondary, fontSize = 13.sp)
        }
        Spacer(Modifier.height(10.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            item {
                NewsCard(
                    image = Res.drawable.news_spring,
                    title = "Spring to Life",
                    subtitle = "A fresh new look for Minecraft!",
                    date = "Mar 25, 2025"
                )
            }
            item {
                NewsCard(
                    image = Res.drawable.news_trials,
                    title = "The Tricky Trials Update",
                    subtitle = "Take on new challenges!",
                    date = "Jun 13, 2024"
                )
            }
            item {
                NewsCard(
                    image = Res.drawable.news_camel,
                    title = "Camel! Ride in Style",
                    subtitle = "Explore the desert with camels.",
                    date = "Apr 3, 2024"
                )
            }
        }
    }
}

@Composable
private fun NewsCard(
    image: DrawableResource,
    title: String,
    subtitle: String,
    date: String
) {
    Card(
        modifier = Modifier.width(218.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box {
                Image(
                    painter = painterResource(image),
                    contentDescription = title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(112.dp),
                    contentScale = ContentScale.Crop
                )
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
                Text(date, fontSize = 11.sp, color = TextSecondary)
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
            Image(
                painter = painterResource(Res.drawable.hero_nether),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(104.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.height(10.dp))
            Text("The Nether Update", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Text("New biomes, new features!", fontSize = 12.sp, color = TextSecondary)
            Spacer(Modifier.height(4.dp))
            Text("Jun 30, 2024", fontSize = 11.sp, color = TextSecondary)
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
                QuickLinkTile("🌐", "Official Website", Modifier.weight(1f))
                QuickLinkTile("📄", "Patch Notes", Modifier.weight(1f))
                QuickLinkTile("💬", "Discord", Modifier.weight(1f))
            }
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                QuickLinkTile("🛒", "Marketplace", Modifier.weight(1f))
                QuickLinkTile("👕", "Skins", Modifier.weight(1f))
                QuickLinkTile("🎁", "Gift Codes", Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun QuickLinkTile(icon: String, label: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.height(68.dp),
        shape = RoundedCornerShape(12.dp),
        color = SoftCream
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(icon, fontSize = 16.sp)
            Spacer(Modifier.height(4.dp))
            Text(
                label,
                fontSize = 10.sp,
                color = TextPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
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
            Spacer(Modifier.height(6.dp))
            Text(
                "Play with friends anywhere, anytime!",
                fontSize = 13.sp,
                color = TextSecondary
            )
            Spacer(Modifier.height(10.dp))
            Image(
                painter = painterResource(Res.drawable.realms_island),
                contentDescription = "Realms",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A2A2A)),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth().height(42.dp)
            ) {
                Text("Learn More →", color = Color.White, fontWeight = FontWeight.Medium)
            }
        }
    }
}
