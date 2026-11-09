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
import com.aylauncher.data.InstallState
import com.aylauncher.data.VersionsState
import com.aylauncher.domain.LoaderType
import com.aylauncher.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun InstallationsScreen(
    onCreateNew: () -> Unit = {}
) {
    val instances by AppState.instances.instances.collectAsState()
    val installState by AppState.installer.state.collectAsState()
    val versionsState by AppState.versions.state.collectAsState()
    val scope = rememberCoroutineScope()

    var showNewInstance by remember { mutableStateOf(false) }
    var showCreateForm by remember { mutableStateOf(false) }

    // Ensure versions are loaded (needed for install URLs)
    LaunchedEffect(Unit) {
        if (versionsState is VersionsState.Idle) {
            AppState.versions.refresh()
        }
    }

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

        Spacer(Modifier.height(16.dp))

        // Install progress banner
        when (val s = installState) {
            is InstallState.Preparing -> {
                InstallBanner("Preparing ${s.versionId}…", progress = null)
            }
            is InstallState.Downloading -> {
                val prog = if (s.total > 0) s.completed.toFloat() / s.total else 0f
                InstallBanner(
                    "Downloading ${s.versionId}: ${s.current} (${s.completed}/${s.total})",
                    progress = prog
                )
            }
            is InstallState.Success -> {
                InstallBanner("Installed ${s.versionId} → ${s.gameDir}", progress = 1f, success = true)
                LaunchedEffect(s) {
                    kotlinx.coroutines.delay(4000)
                    AppState.installer.reset()
                }
            }
            is InstallState.Error -> {
                InstallBanner("Error: ${s.message}", progress = null, error = true)
                TextButton(onClick = { AppState.installer.reset() }) {
                    Text("Dismiss")
                }
            }
            else -> {}
        }

        Spacer(Modifier.height(12.dp))

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
                        // Install version files
                        OutlinedButton(
                            onClick = {
                                val url = findVersionUrl(inst.minecraftVersion, versionsState)
                                if (url != null) {
                                    scope.launch {
                                        AppState.installer.install(inst.minecraftVersion, url)
                                    }
                                }
                            },
                            enabled = installState is InstallState.Idle || installState is InstallState.Success || installState is InstallState.Error,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Install", fontSize = 13.sp)
                        }
                        Spacer(Modifier.width(8.dp))
                        Button(
                            onClick = { /* launch later */ },
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
                onCreate = { name, version, loader, ram ->
                    AppState.instances.create(
                        name = name,
                        minecraftVersion = version,
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
private fun InstallBanner(
    text: String,
    progress: Float?,
    success: Boolean = false,
    error: Boolean = false
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                error -> Color(0xFFFFEBEE)
                success -> Color(0xFFE8F5E9)
                else -> SoftCream
            }
        )
    ) {
        Column(Modifier.padding(14.dp)) {
            Text(
                text,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = when {
                    error -> Color(0xFFC62828)
                    success -> Color(0xFF2E7D32)
                    else -> TextPrimary
                }
            )
            if (progress != null) {
                Spacer(Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                    color = if (success) OnlineGreen else WarmYellow,
                    trackColor = Color.White
                )
            }
        }
    }
}

private fun findVersionUrl(versionId: String, state: VersionsState): String? {
    return when (state) {
        is VersionsState.Ready -> state.all.find { it.id == versionId }?.url?.takeIf { it.isNotBlank() }
        else -> null
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
    onCreate: (name: String, version: String, loader: LoaderType, ramMb: Int) -> Unit = { _, _, _, _ -> }
) {
    var name by remember { mutableStateOf("New Instance") }
    var ram by remember { mutableStateOf(4096f) }
    var selectedLoader by remember { mutableStateOf("FABRIC") }
    var selectedVersion by remember { mutableStateOf("1.21.3") }
    var showSnapshots by remember { mutableStateOf(false) }

    val versionsState by AppState.versions.state.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        if (versionsState is VersionsState.Idle || versionsState is VersionsState.Error) {
            AppState.versions.refresh()
        }
    }

    LaunchedEffect(versionsState) {
        when (val s = versionsState) {
            is VersionsState.Ready -> {
                if (selectedVersion.isBlank() || selectedVersion == "1.21.3") {
                    selectedVersion = s.latestRelease
                }
            }
            is VersionsState.Error -> {
                if (selectedVersion.isBlank()) {
                    selectedVersion = s.fallback.firstOrNull()?.id ?: "1.21.3"
                }
            }
            else -> {}
        }
    }

    val loaders = listOf("VANILLA", "FABRIC", "FORGE", "NEOFORGE", "QUILT", "OPTIFINE")

    val versionOptions: List<String> = when (val s = versionsState) {
        is VersionsState.Ready -> {
            if (showSnapshots) s.snapshots.take(30).map { it.id }
            else s.releases.take(40).map { it.id }
        }
        is VersionsState.Error -> s.fallback.map { it.id }
        else -> listOf("1.21.3", "1.21.1", "1.20.6")
    }

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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("MINECRAFT VERSION", fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.weight(1f))
                when (versionsState) {
                    is VersionsState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp,
                            color = WarmYellow
                        )
                        Spacer(Modifier.width(6.dp))
                        Text("Loading…", fontSize = 12.sp, color = TextSecondary)
                    }
                    is VersionsState.Ready -> {
                        Text(
                            "Latest: ${(versionsState as VersionsState.Ready).latestRelease}",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                    is VersionsState.Error -> {
                        Text("Offline list", fontSize = 12.sp, color = Color(0xFFE53935))
                        TextButton(onClick = { scope.launch { AppState.versions.refresh(force = true) } }) {
                            Text("Retry", fontSize = 12.sp)
                        }
                    }
                    else -> {}
                }
            }

            Spacer(Modifier.height(8.dp))
            Surface(
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                color = SoftCream
            ) {
                Row(
                    Modifier.padding(horizontal = 16.dp).fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(selectedVersion, Modifier.weight(1f), fontWeight = FontWeight.Medium)
                    Text("▼", color = TextSecondary)
                }
            }

            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                FilterChip(
                    selected = !showSnapshots,
                    onClick = { showSnapshots = false },
                    label = { Text("Releases", fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = GoldButton)
                )
                Spacer(Modifier.width(8.dp))
                FilterChip(
                    selected = showSnapshots,
                    onClick = { showSnapshots = true },
                    label = { Text("Snapshots", fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = GoldButton)
                )
            }

            Spacer(Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                versionOptions.take(8).forEach { id ->
                    FilterChip(
                        selected = selectedVersion == id,
                        onClick = { selectedVersion = id },
                        label = { Text(id, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GoldButton
                        )
                    )
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
                        onCreate(name, selectedVersion, loader, ram.toInt())
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
