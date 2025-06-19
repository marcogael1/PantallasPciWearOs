package com.example.plantillasproyecto.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Colores principales
val PCIBlue = Color(0xFF0057D9)
val DarkGray = Color(0xFF374151)

@Composable
fun PCIStoreTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = PCIBlue,
            background = Color.Black,
            surface = DarkGray,
            onSurface = Color.White
        ),
        content = content
    )
}