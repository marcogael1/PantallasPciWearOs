package com.example.plantillasproyecto.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.plantillasproyecto.data.StoreData
import com.example.plantillasproyecto.ui.components.*
import com.example.plantillasproyecto.ui.theme.*
import com.example.plantillasproyecto.utils.MapsUtils

@Composable
fun StoresScreen(navController: NavHostController) {
    val screenConfig = rememberScreenConfig()

    ScreenWithHeader(
        title = "Tiendas Cercanas",
        screenConfig = screenConfig,
        onBackClick = { navController.popBackStack() }
    ) {
        val stores = listOf(
            StoreData("PCI Centro", "0.5 km", "9:00 - 21:00", "Abierto", 4.8f, 21.1417365, -98.4200389),
            StoreData("PCI Centro 2", "1.2 km", "10:00 - 22:00", "Abierto", 4.6f, 21.1420000, -98.4190000),
            StoreData("PCI Norte", "2.8 km", "9:00 - 20:00", "Cerrado", 4.7f, 21.1450000, -98.4150000),
            StoreData("PCI Express", "3.1 km", "24 horas", "Abierto", 4.5f, 21.1400000, -98.4250000)
        )

        LazyColumn(
            contentPadding = PaddingValues(screenConfig.padding),
            verticalArrangement = Arrangement.spacedBy(screenConfig.spacing)
        ) {
            items(stores) { store ->
                EnhancedStoreCard(store = store, screenConfig = screenConfig)
            }
        }
    }
}

@Composable
fun EnhancedStoreCard(store: StoreData, screenConfig: ScreenConfig) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(screenConfig.cornerRadius)),
        shape = RoundedCornerShape(screenConfig.cornerRadius),
        colors = CardDefaults.cardColors(containerColor = DarkGray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(screenConfig.cardPadding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(screenConfig.spacing / 2)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(screenConfig.iconSize),
                        tint = PCIBlue
                    )

                    Text(
                        text = store.name,
                        fontSize = screenConfig.bodySize,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(screenConfig.spacing / 2))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(screenConfig.spacing)
                ) {
                    Text(
                        text = "📍 ${store.distance}",
                        fontSize = screenConfig.captionSize,
                        color = Color(0xFFD1D5DB)
                    )

                    RatingDisplay(rating = store.rating, screenConfig = screenConfig)
                }

                Spacer(modifier = Modifier.height(screenConfig.spacing / 3))

                Text(
                    text = "🕒 ${store.hours}",
                    fontSize = screenConfig.captionSize,
                    color = Color(0xFF9CA3AF)
                )

                Spacer(modifier = Modifier.height(screenConfig.spacing / 2))

                StoreStatusBadge(status = store.status, screenConfig = screenConfig)
            }

            Box(
                modifier = Modifier
                    .size(screenConfig.iconSize * 2f)
                    .background(PCIBlue.copy(alpha = 0.2f), RoundedCornerShape(screenConfig.cornerRadius / 2))
                    .border(1.dp, PCIBlue.copy(alpha = 0.5f), RoundedCornerShape(screenConfig.cornerRadius / 2))
                    .clickable {
                        MapsUtils.openGoogleMaps(
                            context = context,
                            latitude = store.latitude,
                            longitude = store.longitude,
                            label = store.name
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Navigation,
                    contentDescription = "Abrir en Google Maps",
                    modifier = Modifier.size(screenConfig.iconSize),
                    tint = PCIBlue
                )
            }
        }
    }
}

@Composable
fun StoreStatusBadge(status: String, screenConfig: ScreenConfig) {
    val (color, icon) = when (status) {
        "Abierto" -> Color(0xFF10B981) to Icons.Default.CheckCircle
        "Cerrado" -> Color(0xFFEF4444) to Icons.Default.Cancel
        else -> Color.Gray to Icons.Default.Help
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(screenConfig.spacing / 3),
        modifier = Modifier
            .background(color.copy(alpha = 0.2f), RoundedCornerShape(screenConfig.cornerRadius / 2))
            .border(1.dp, color.copy(alpha = 0.5f), RoundedCornerShape(screenConfig.cornerRadius / 2))
            .padding(horizontal = screenConfig.spacing / 2, vertical = screenConfig.spacing / 4)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(screenConfig.iconSize * 0.6f),
            tint = color
        )
        Text(
            text = status,
            fontSize = screenConfig.captionSize * 0.9f,
            color = color,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun RatingDisplay(rating: Float, screenConfig: ScreenConfig) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(screenConfig.spacing / 4)
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            modifier = Modifier.size(screenConfig.iconSize * 0.7f),
            tint = Color(0xFFF59E0B)
        )
        Text(
            text = rating.toString(),
            fontSize = screenConfig.captionSize,
            color = Color(0xFF9CA3AF),
            fontWeight = FontWeight.Medium
        )
    }
}