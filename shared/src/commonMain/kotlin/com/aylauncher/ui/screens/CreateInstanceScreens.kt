package com.aylauncher.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aylauncher.data.AppState
import com.aylauncher.domain.LoaderType
import com.aylauncher.ui.theme.*

@Composable
fun InstallationsScreen(
    onCreateNew: () -> Unit = {}
) {
    val instances by AppState.instances.instances.collectAsState()
    var showNewInstance by remember { mutableStateOf(false) }
    var showCreateForm by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Installations", fontWeight = FontWeight.Bold, fontSize = 24.sp)
            Spacer(Modifier.weight(1f))
            Button(
                onClick = { showNewInstance = true },
                colors = ButtonDefaults.buttonColors(containerColor = GoldButton),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("+ New Instance", color = TextPrimary, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(Modifier.height(20.dp))

        if (instances.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SoftCream)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("📦", fontSize = 48.sp)
                    Spacer(Modifier.height(12.dp))
                    Text("No instances yet", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                    Text(
                        "Create a Normal or Modpack instance to get started.",
                        color = TextSecondary,
                        fontSize = 14.sp
                    )
                    Spacer(Modifier.height(20.dp))
                    Button(
                        onClick = { showNewInstance = true },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldButton),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text("Create your first instance →", color = TextPrimary, fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            instances.forEach { inst ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBackground)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🟩", fontSize = 28.sp)
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(inst.name, fontWeight = FontWeight.SemiBold)
                            Text(
                                "${inst.loader} • ${inst.minecraftVersion} • ${inst.ramMb} MB",
                                fontSize = 13.sp,
                                color = TextSecondary
                            )
                        }
                        Button(
                            onClick = { /* play */ },
                            colors = ButtonDefaults.buttonColors(containerColor = GoldButton),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Play", color = TextPrimary, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        if (showNewInstance && !showCreateForm) {
            Spacer(Modifier.height(24.dp))
            NewInstanceDialogContent(
                onSelectNormal = { showCreateForm = true },
                onSelectModpack = { showNewInstance = false },
                onDismiss = { showNewInstance = false }
            )
        }

        if (showCreateForm) {
            Spacer(Modifier.height(24.dp))
            CreateInstanceForm(
                onCancel = {
                    showCreateForm = false
                    showNewInstance = false
                },
                onCreate = { name, loader, ram ->
                    AppState.instances.create(
                        name = name,
                        minecraftVersion = "1.21.3",
                        loader = loader,
                        ramMb = ram
                    )
                    showCreateForm = false
                    showNewInstance = false
                }
            )
        }
    }
}

@Composable
fun NewInstanceDialogContent(
    onSelectNormal: () -> Unit = {},
    onSelectModpack: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("NEW INSTANCE", fontWeight = FontWeight.Bold, color = WarmYellow)
                Spacer(Modifier.weight(1f))
                TextButton(onClick = onDismiss) {
                    Text("✕", fontSize = 18.sp)
                }
            }
            Spacer(Modifier.height(6.dp))
            Text("What do you want to create?", fontWeight = FontWeight.Bold, fontSize = 22.sp)
            Spacer(Modifier.height(24.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                InstanceTypeCard(
                    title = "Normal",
                    subtitle = "Vanilla, Fabric, Forge, Quilt or NeoForge.",
                    icon = "🟩",
                    onSelect = onSelectNormal
                )
                InstanceTypeCard(
                    title = "Modpack",
                    subtitle = "Browse and install modpacks.",
                    icon = "📦",
                    onSelect = onSelectModpack
                )
            }
        }
    }
}

@Composable
fun InstanceTypeCard(
    title: String,
    subtitle: String,
    icon: String,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier.width(260.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = SoftCream)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
fun CreateInstanceForm(
    onCancel: () -> Unit = {},
    onCreate: (name: String, loader: LoaderType, ramMb: Int) -> Unit = { _, _, _ -> }
) {
    var name by remember { mutableStateOf("Fabric 1.21") }
    var ram by remember { mutableStateOf(4096f) }
    var selectedLoader by remember { mutableStateOf("FABRIC") }

    val loaders = listOf("VANILLA", "FABRIC", "FORGE", "NEOFORGE", "QUILT", "OPTIFINE")

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(Modifier.padding(28.dp)) {
            Text("CREATE INSTANCE", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text("NORMAL PROFILE • $selectedLoader", fontSize = 13.sp, color = TextSecondary)
            Spacer(Modifier.height(20.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("NAME") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(Modifier.height(16.dp))
            Text("LOADER", fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                loaders.forEach { loader ->
                    val selected = loader == selectedLoader
                    FilterChip(
                        selected = selected,
                        onClick = { selectedLoader = loader },
                        label = { Text(loader, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GoldButton,
                            selectedLabelColor = TextPrimary
                        )
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
            Text("MINECRAFT VERSION", fontWeight = FontWeight.SemiBold)
            Surface(
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                color = SoftCream
            ) {
                Row(
                    Modifier.padding(horizontal = 16.dp).fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("1.21.3", Modifier.weight(1f))
                    Text("▼", color = TextSecondary)
                }
            }

            Spacer(Modifier.height(16.dp))
            Text("LOADER VERSION", fontWeight = FontWeight.SemiBold)
            Surface(
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                color = SoftCream
            ) {
                Row(
                    Modifier.padding(horizontal = 16.dp).fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("LATEST", Modifier.weight(1f))
                    Text("▼", color = TextSecondary)
                }
            }

            Spacer(Modifier.height(20.dp))
            Text("MEMORY (RAM)", fontWeight = FontWeight.SemiBold)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Slider(
                    value = ram,
                    onValueChange = { ram = it },
                    valueRange = 1024f..16384f,
                    steps = 14,
                    modifier = Modifier.weight(1f),
                    colors = SliderDefaults.colors(
                        thumbColor = WarmYellow,
                        activeTrackColor = WarmYellow
                    )
                )
                Text("${ram.toInt()} MB", fontWeight = FontWeight.Bold, modifier = Modifier.width(80.dp))
            }

            Spacer(Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Use launcher default", fontSize = 13.sp, color = TextSecondary)
                Spacer(Modifier.weight(1f))
                Switch(
                    checked = true,
                    onCheckedChange = {},
                    colors = SwitchDefaults.colors(checkedTrackColor = OnlineGreen)
                )
            }

            Spacer(Modifier.height(24.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("CANCEL")
                }
                Button(
                    onClick = {
                        val loader = when (selectedLoader) {
                            "FABRIC" -> LoaderType.FABRIC
                            "FORGE" -> LoaderType.FORGE
                            "NEOFORGE" -> LoaderType.NEOFORGE
                            "QUILT" -> LoaderType.QUILT
                            "OPTIFINE" -> LoaderType.OPTIFINE
                            else -> LoaderType.VANILLA
                        }
                        onCreate(name, loader, ram.toInt())
                    },
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
