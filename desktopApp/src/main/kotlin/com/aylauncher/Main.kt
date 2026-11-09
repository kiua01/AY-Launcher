package com.aylauncher

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.compose.ui.unit.dp
import com.aylauncher.ui.AYLauncherApp

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "AY Launcher",
        state = rememberWindowState(width = 1280.dp, height = 800.dp)
    ) {
        AYLauncherApp()
    }
}
