package com.aylauncher.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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

data class FileItem(val name: String, val size: String, val isFolder: Boolean)

@Composable
fun FileManagerScreen() {
    val items = listOf(
        FileItem(".minecraft", "2.4 GB", true),
        FileItem("games", "842 MB", true),
        FileItem("mods", "1.2 GB", true),
        FileItem("resourcepacks", "523 MB", true),
        FileItem("shaderpacks", "378 MB", true),
        FileItem("options.txt", "4 KB", false),
        FileItem("servers.dat", "12 KB", false),
        FileItem("screenshot.png", "2.1 MB", false),
        FileItem("modpack.zip", "56 MB", false),
        FileItem("latest.log", "128 KB", false),
    )

    Column(Modifier.fillMaxSize()) {
        // Header banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.horizontalGradient(listOf(Color(0xFF8B0000), Color(0xFFFF4500)))
                )
                .padding(24.dp)
        ) {
            Column {
                Text("📁 File Manager", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 26.sp)
                Text("Browse, manage and organize your files.", color = Color.White.copy(0.9f))
            }
        }

        Spacer(Modifier.height(16.dp))

        // Toolbar
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = {}, shape = RoundedCornerShape(12.dp)) { Text("← Back") }
            OutlinedButton(onClick = {}, shape = RoundedCornerShape(12.dp)) { Text("↑ Up") }
            Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = SoftCream), shape = RoundedCornerShape(12.dp)) {
                Text("+ New Folder", color = TextPrimary)
            }
            Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = SoftCream), shape = RoundedCornerShape(12.dp)) {
                Text("+ New File", color = TextPrimary)
            }
        }

        Spacer(Modifier.height(16.dp))

        // Breadcrumb + grid
        Text("Home  >  .minecraft", color = TextSecondary, fontSize = 13.sp)
        Spacer(Modifier.height(12.dp))

        LazyVerticalGrid(
            columns = GridCells.Adaptive(140.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(items) { item ->
                FileCard(item)
            }
        }

        // Storage bar
        Spacer(Modifier.height(12.dp))
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SoftCream)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Storage Usage: 4.8 GB / 12 GB", fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { 4.8f / 12f },
                    modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                    color = WarmYellow,
                    trackColor = Color.White
                )
            }
        }
    }
}

@Composable
fun FileCard(item: FileItem) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        modifier = Modifier.height(110.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(if (item.isFolder) "📁" else "📄", fontSize = 32.sp)
            Text(item.name, fontWeight = FontWeight.Medium, maxLines = 1, fontSize = 13.sp)
            Text(item.size, fontSize = 11.sp, color = TextSecondary)
        }
    }
}
