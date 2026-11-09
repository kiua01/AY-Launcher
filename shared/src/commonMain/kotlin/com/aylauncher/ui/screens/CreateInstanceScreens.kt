package com.aylauncher.ui.screens

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
import com.aylauncher.ui.theme.*

@Composable
fun InstallationsScreen() {
    // Simple entry that can lead to New Instance
    Column(Modifier.fillMaxSize().padding(8.dp)) {
        Text("Installations", fontWeight = FontWeight.Bold, fontSize = 24.sp)
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { /* open New Instance dialog */ },
            colors = ButtonDefaults.buttonColors(containerColor = GoldButton),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("+ New Instance", color = TextPrimary, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(24.dp))
        Text("No instances yet. Create one!", color = TextSecondary)
        // In full version: list of instances cards
    }
}

@Composable
fun NewInstanceDialogContent() {
    // Matches the "What do you want to create?" screen with fox / wolf
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(CardBackground)
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("NEW INSTANCE", fontWeight = FontWeight.Bold, color = WarmYellow)
        Spacer(Modifier.height(8.dp))
        Text("What do you want to create?", fontWeight = FontWeight.Bold, fontSize = 24.sp)
        Spacer(Modifier.height(24.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            InstanceTypeCard(
                title = "Normal",
                subtitle = "Vanilla, Fabric, Forge, Quilt or NeoForge.",
                icon = "🟩",
                onSelect = {}
            )
            InstanceTypeCard(
                title = "Modpack",
                subtitle = "Browse and install modpacks.",
                icon = "📦",
                onSelect = {}
            )
        }
    }
}

@Composable
fun InstanceTypeCard(title: String, subtitle: String, icon: String, onSelect: () -> Unit) {
    Card(
        modifier = Modifier.width(260.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = SoftCream)
    ) {
        Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFFFE0B2)),
                contentAlignment = Alignment.Center
            ) {
                Text(icon, fontSize = 56.sp)
            }
            Spacer(Modifier.height(12.dp))
            Text(title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(subtitle, fontSize = 13.sp, color = TextSecondary)
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onSelect,
                colors = ButtonDefaults.buttonColors(containerColor = GoldButton),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Select →", color = TextPrimary, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun CreateInstanceForm() {
    // Matches the detailed CREATE INSTANCE form (Fabric example)
    var name by remember { mutableStateOf("Fabric 26.3") }
    var ram by remember { mutableStateOf(5000f) }

    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground)
    ) {
        Column(Modifier.padding(28.dp)) {
            Text("CREATE INSTANCE", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text("NORMAL PROFILE • FABRIC", fontSize = 13.sp, color = TextSecondary)
            Spacer(Modifier.height(20.dp))

            // Name
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("NAME") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(Modifier.height(16.dp))
            Text("LOADER", fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("VANILLA", "FABRIC", "FORGE", "NEOFORGE", "QUILT", "OPTIFINE").forEach { loader ->
                    val selected = loader == "FABRIC"
                    FilterChip(
                        selected = selected,
                        onClick = {},
                        label = { Text(loader, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GoldButton
                        )
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
            Text("MINECRAFT VERSION", fontWeight = FontWeight.SemiBold)
            // Dropdown placeholder
            Surface(
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                color = SoftCream
            ) {
                Row(Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("26.3", Modifier.weight(1f))
                    Text("▼")
                }
            }

            Spacer(Modifier.height(16.dp))
            Text("LOADER VERSION", fontWeight = FontWeight.SemiBold)
            Surface(
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                color = SoftCream
            ) {
                Row(Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("LATEST (0.20.1)", Modifier.weight(1f))
                    Text("▼")
                }
            }

            Spacer(Modifier.height(20.dp))
            Text("MEMORY (RAM)", fontWeight = FontWeight.SemiBold)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Slider(
                    value = ram,
                    onValueChange = { ram = it },
                    valueRange = 1024f..16384f,
                    modifier = Modifier.weight(1f)
                )
                Text("${ram.toInt()} MB", fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(24.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = {},
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("CANCEL")
                }
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("+ CREATE", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
