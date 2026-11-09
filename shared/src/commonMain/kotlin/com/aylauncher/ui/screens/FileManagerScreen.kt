package com.aylauncher.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aylauncher.data.FsEntry
import com.aylauncher.data.createFileBrowser
import com.aylauncher.ui.theme.*

@Composable
fun FileManagerScreen() {
    val browser = remember { createFileBrowser() }
    var currentPath by remember { mutableStateOf(browser.rootPath) }
    var entries by remember { mutableStateOf(browser.list(currentPath)) }
    var error by remember { mutableStateOf<String?>(null) }

    fun navigateTo(path: String) {
        if (!browser.isUnderRoot(path)) {
            error = "Cannot leave the launcher data folder"
            return
        }
        error = null
        currentPath = path
        entries = browser.list(path)
    }

    fun goUp() {
        val parent = browser.parent(currentPath)
        if (parent != null) navigateTo(parent)
    }

    fun refresh() {
        entries = browser.list(currentPath)
    }

    // Relative path label for breadcrumb
    val relative = remember(currentPath) {
        val root = browser.rootPath
        if (currentPath == root) "Home"
        else {
            val rel = currentPath.removePrefix(root).trimStart('/', '\\')
            "Home / ${rel.replace('\\', '/')}"
        }
    }

    Column(Modifier.fillMaxSize()) {
        // Header banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.horizontalGradient(listOf(Color(0xFF8B0000), Color(0xFFFF4500)))
                )
                .padding(22.dp)
        ) {
            Column {
                Text("📁 File Manager", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                Text(
                    "Browse files under your launcher data folder.",
                    color = Color.White.copy(0.9f),
                    fontSize = 13.sp
                )
            }
        }

        Spacer(Modifier.height(14.dp))

        // Toolbar
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = { goUp() },
                enabled = browser.parent(currentPath) != null,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("↑ Up")
            }
            OutlinedButton(
                onClick = { navigateTo(browser.rootPath) },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Home")
            }
            OutlinedButton(
                onClick = { refresh() },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Refresh")
            }
            Spacer(Modifier.weight(1f))
            Text(
                "${entries.size} items",
                fontSize = 12.sp,
                color = TextSecondary
            )
        }

        Spacer(Modifier.height(10.dp))

        // Breadcrumb
        Text(
            relative,
            color = TextSecondary,
            fontSize = 13.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        if (error != null) {
            Spacer(Modifier.height(8.dp))
            Text(error!!, color = Color(0xFFE53935), fontSize = 13.sp)
        }

        Spacer(Modifier.height(12.dp))

        if (entries.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SoftCream)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("This folder is empty", color = TextSecondary)
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(140.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(entries, key = { it.path }) { entry ->
                    FileCard(
                        entry = entry,
                        onClick = {
                            if (entry.isDirectory) {
                                navigateTo(entry.path)
                            }
                            // Files: no open action yet
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // Footer info
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SoftCream)
        ) {
            Column(Modifier.padding(14.dp)) {
                Text(
                    "Root: ${browser.rootPath}",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    "You cannot browse outside this folder.",
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }
        }
    }
}

@Composable
fun FileCard(entry: FsEntry, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        modifier = Modifier
            .height(110.dp)
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                if (entry.isDirectory) "📁" else fileIcon(entry.name),
                fontSize = 30.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                entry.name,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 13.sp
            )
            Text(
                entry.sizeLabel,
                fontSize = 11.sp,
                color = TextSecondary
            )
        }
    }
}

private fun fileIcon(name: String): String {
    val lower = name.lowercase()
    return when {
        lower.endsWith(".jar") || lower.endsWith(".zip") -> "📦"
        lower.endsWith(".png") || lower.endsWith(".jpg") -> "🖼️"
        lower.endsWith(".txt") || lower.endsWith(".log") -> "📄"
        lower.endsWith(".json") -> "📋"
        else -> "📄"
    }
}
