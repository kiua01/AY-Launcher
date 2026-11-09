package com.aylauncher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.aylauncher.data.AppState
import com.aylauncher.ui.AYLauncherApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppState.bootstrap()
        enableEdgeToEdge()
        setContent {
            AYLauncherApp()
        }
    }
}
