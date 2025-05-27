package com.example.plantillasproyecto.ui.Theme

import androidx.compose.runtime.Composable
import androidx.wear.compose.material.MaterialTheme

@Composable
fun PCIStoreTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = wearColorPalette,
        typography = Typography,
        content = content
    )
}

private val wearColorPalette = androidx.wear.compose.material.Colors(
    primary = androidx.compose.ui.graphics.Color(0xFF0057D9),
    primaryVariant = androidx.compose.ui.graphics.Color(0xFF003A91),
    secondary = androidx.compose.ui.graphics.Color(0xFF0057D9),
    secondaryVariant = androidx.compose.ui.graphics.Color(0xFF003A91),
    background = androidx.compose.ui.graphics.Color.Black,
    surface = androidx.compose.ui.graphics.Color(0xFF374151),
    error = androidx.compose.ui.graphics.Color(0xFFDC2626),
    onPrimary = androidx.compose.ui.graphics.Color.White,
    onSecondary = androidx.compose.ui.graphics.Color.White,
    onBackground = androidx.compose.ui.graphics.Color.White,
    onSurface = androidx.compose.ui.graphics.Color.White,
    onSurfaceVariant = androidx.compose.ui.graphics.Color(0xFFD1D5DB),
    onError = androidx.compose.ui.graphics.Color.White
)