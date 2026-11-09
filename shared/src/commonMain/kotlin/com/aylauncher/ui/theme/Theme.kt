package com.aylauncher.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AyLightColorScheme = lightColorScheme(
    primary = WarmYellow,
    onPrimary = TextPrimary,
    secondary = GoldButton,
    onSecondary = TextPrimary,
    background = CreamBackground,
    onBackground = TextPrimary,
    surface = CardBackground,
    onSurface = TextPrimary,
    surfaceVariant = SoftCream,
    onSurfaceVariant = TextSecondary,
    primaryContainer = SidebarSelected,
    secondaryContainer = Color(0xFFFFECB3),
)

@Composable
fun AYLauncherTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // For now force the warm light theme that matches the screenshots.
    // Dark theme can be added later under Settings > Launcher Appearance.
    MaterialTheme(
        colorScheme = AyLightColorScheme,
        content = content
    )
}
