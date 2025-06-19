package com.example.plantillasproyecto.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.plantillasproyecto.ui.theme.*

@Composable
fun ScreenWithHeader(
    title: String,
    screenConfig: ScreenConfig,
    onBackClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(Color(0xFF1A1A2E), Color.Black)
                        )
                    )
                    .padding(screenConfig.padding),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(screenConfig.iconSize * 1.6f)
                        .background(Color(0xFF374151), RoundedCornerShape(screenConfig.cornerRadius / 2))
                        .clickable { onBackClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier.size(screenConfig.iconSize * 0.8f),
                        tint = Color.White
                    )
                }

                Text(
                    text = title,
                    fontSize = screenConfig.bodySize * 1.2f,
                    fontWeight = FontWeight.Bold,
                    color = PCIBlue,
                    letterSpacing = 0.5.sp
                )

                Spacer(modifier = Modifier.width(screenConfig.iconSize * 1.6f))
            }

            content()
        }
    }
}

@Composable
fun rememberScreenConfig(): ScreenConfig {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    return when {
        screenWidth <= 320.dp -> ScreenConfig.Small
        screenWidth <= 390.dp -> ScreenConfig.Medium
        else -> ScreenConfig.Large
    }
}

enum class ScreenConfig(
    val padding: Dp,
    val cardPadding: Dp,
    val buttonHeight: Dp,
    val iconSize: Dp,
    val titleSize: TextUnit,
    val bodySize: TextUnit,
    val captionSize: TextUnit,
    val spacing: Dp,
    val cornerRadius: Dp
) {
    Small(
        padding = 12.dp,
        cardPadding = 10.dp,
        buttonHeight = 48.dp,
        iconSize = 16.dp,
        titleSize = 16.sp,
        bodySize = 10.sp,
        captionSize = 8.sp,
        spacing = 8.dp,
        cornerRadius = 8.dp
    ),
    Medium(
        padding = 16.dp,
        cardPadding = 12.dp,
        buttonHeight = 56.dp,
        iconSize = 20.dp,
        titleSize = 18.sp,
        bodySize = 11.sp,
        captionSize = 9.sp,
        spacing = 10.dp,
        cornerRadius = 10.dp
    ),
    Large(
        padding = 20.dp,
        cardPadding = 16.dp,
        buttonHeight = 64.dp,
        iconSize = 24.dp,
        titleSize = 20.sp,
        bodySize = 12.sp,
        captionSize = 10.sp,
        spacing = 12.dp,
        cornerRadius = 12.dp
    )
}

// Pantallas de estado comunes para búsqueda por voz
@Composable
fun PermissionDeniedScreen(
    onRequestPermission: () -> Unit,
    screenConfig: ScreenConfig
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(screenConfig.spacing)
        ) {
            Icon(
                imageVector = Icons.Default.MicOff,
                contentDescription = null,
                modifier = Modifier.size(screenConfig.iconSize * 3f),
                tint = Color(0xFFEF4444)
            )

            Text(
                text = "Permiso de Micrófono",
                fontSize = screenConfig.bodySize,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Necesitamos acceso al micrófono para la búsqueda por voz",
                fontSize = screenConfig.captionSize,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                lineHeight = (screenConfig.captionSize.value * 1.3f).sp
            )

            Button(
                onClick = onRequestPermission,
                colors = ButtonDefaults.buttonColors(containerColor = PCIBlue)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(screenConfig.spacing / 2)
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = null,
                        modifier = Modifier.size(screenConfig.iconSize * 0.8f)
                    )
                    Text(
                        text = "Permitir Micrófono",
                        fontSize = screenConfig.captionSize
                    )
                }
            }
        }
    }
}

@Composable
fun MicrophoneNotAvailableScreen(screenConfig: ScreenConfig) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(screenConfig.spacing)
        ) {
            Icon(
                imageVector = Icons.Default.MicNone,
                contentDescription = null,
                modifier = Modifier.size(screenConfig.iconSize * 3f),
                tint = Color(0xFFF59E0B)
            )

            Text(
                text = "Micrófono No Disponible",
                fontSize = screenConfig.bodySize,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Configura el micrófono en el emulador:\n\n1. Extended Controls (⋯)\n2. Microphone\n3. Activar las 3 opciones",
                fontSize = screenConfig.captionSize,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                lineHeight = (screenConfig.captionSize.value * 1.3f).sp
            )
        }
    }
}

@Composable
fun LoadingProductsScreen(screenConfig: ScreenConfig) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(screenConfig.spacing)
        ) {
            CircularProgressIndicator(
                color = PCIBlue,
                modifier = Modifier.size(screenConfig.iconSize * 2f)
            )
            Text(
                text = "Cargando productos...",
                fontSize = screenConfig.bodySize,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ErrorProductsScreen(
    error: String,
    onRetry: () -> Unit,
    screenConfig: ScreenConfig
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(screenConfig.spacing)
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = null,
                modifier = Modifier.size(screenConfig.iconSize * 2f),
                tint = Color(0xFFEF4444)
            )

            Text(
                text = "Error al cargar productos",
                fontSize = screenConfig.bodySize,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = error,
                fontSize = screenConfig.captionSize,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )

            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(containerColor = PCIBlue)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(screenConfig.spacing / 2)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(screenConfig.iconSize * 0.8f)
                    )
                    Text(
                        text = "Reintentar",
                        fontSize = screenConfig.captionSize
                    )
                }
            }
        }
    }
}