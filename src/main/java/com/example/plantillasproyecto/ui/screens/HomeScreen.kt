package com.example.plantillasproyecto.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.plantillasproyecto.ui.components.ScreenConfig
import com.example.plantillasproyecto.ui.theme.*
import com.example.plantillasproyecto.ui.components.rememberScreenConfig
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(navController: NavHostController) {
    val screenConfig = rememberScreenConfig()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF1A1A2E),
                            Color.Black
                        ),
                        radius = 800f
                    )
                )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(screenConfig.padding)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(bottom = screenConfig.spacing * 2)
                ) {
                    Text(
                        text = "PCI Tecnologia",
                        fontSize = screenConfig.titleSize,
                        fontWeight = FontWeight.Bold,
                        color = PCIBlue,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Tu tienda tecnológica",
                        fontSize = screenConfig.bodySize,
                        color = Color(0xFF9CA3AF),
                        fontWeight = FontWeight.Light
                    )
                }

                NavigationGrid(navController, screenConfig)
            }
        }
    }
}

@Composable
fun NavigationGrid(navController: NavHostController, screenConfig: ScreenConfig) {
    Column(
        verticalArrangement = Arrangement.spacedBy(screenConfig.spacing)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(screenConfig.spacing),
            modifier = Modifier.fillMaxWidth()
        ) {
            EnhancedNavigationButton(
                icon = Icons.Default.ShoppingCart,
                label = "Pedidos",
                color = Color(0xFF10B981),
                screenConfig = screenConfig,
                modifier = Modifier.weight(1f)
            ) { navController.navigate("orders") }

            EnhancedNavigationButton(
                icon = Icons.Default.Notifications,
                label = "Alertas",
                color = Color(0xFFF59E0B),
                screenConfig = screenConfig,
                modifier = Modifier.weight(1f)
            ) { navController.navigate("notifications") }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(screenConfig.spacing),
            modifier = Modifier.fillMaxWidth()
        ) {
            EnhancedNavigationButton(
                icon = Icons.Default.Mic,
                label = "Buscar",
                color = Color(0xFF8B5CF6),
                screenConfig = screenConfig,
                modifier = Modifier.weight(1f)
            ) { navController.navigate("voice") }

            EnhancedNavigationButton(
                icon = Icons.Default.Favorite,
                label = "Favoritos",
                color = Color(0xFFEF4444),
                screenConfig = screenConfig,
                modifier = Modifier.weight(1f)
            ) { navController.navigate("favorites") }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenConfig.buttonHeight * 0.7f)
                .shadow(8.dp, RoundedCornerShape(screenConfig.cornerRadius))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(PCIBlue, Color(0xFF3B82F6))
                    ),
                    RoundedCornerShape(screenConfig.cornerRadius)
                )
                .clickable { navController.navigate("stores") },
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    modifier = Modifier.size(screenConfig.iconSize * 0.8f),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(screenConfig.spacing / 2))
                Text(
                    text = "Tiendas Cercanas",
                    fontSize = screenConfig.bodySize,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun EnhancedNavigationButton(
    icon: ImageVector,
    label: String,
    color: Color,
    screenConfig: ScreenConfig,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .height(screenConfig.buttonHeight)
            .shadow(6.dp, RoundedCornerShape(screenConfig.cornerRadius))
            .background(
                if (isPressed) DarkGray.copy(alpha = 0.8f) else DarkGray,
                RoundedCornerShape(screenConfig.cornerRadius)
            )
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(screenConfig.cornerRadius))
            .clickable {
                isPressed = true
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(screenConfig.iconSize),
                tint = color
            )
            Spacer(modifier = Modifier.height(screenConfig.spacing / 2))
            Text(
                text = label,
                fontSize = screenConfig.captionSize,
                color = Color.White,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(100)
            isPressed = false
        }
    }
}