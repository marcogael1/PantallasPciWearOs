package com.example.plantillasproyecto.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.plantillasproyecto.data.NotificationData
import com.example.plantillasproyecto.ui.components.*
import com.example.plantillasproyecto.ui.theme.*
import com.example.plantillasproyecto.ui.viewmodels.NotificationsViewModel

@Composable
fun NotificationsScreen(
    navController: NavHostController,
    viewModel: NotificationsViewModel = viewModel()
) {
    val screenConfig = rememberScreenConfig()
    val uiState by viewModel.uiState.collectAsState()

    ScreenWithHeader(
        title = "Notificaciones",
        screenConfig = screenConfig,
        onBackClick = { navController.popBackStack() }
    ) {
        when {
            uiState.isLoading -> {
                LoadingNotificationsScreen(screenConfig)
            }
            uiState.error != null -> {
                ErrorNotificationsScreen(
                    error = uiState.error ?: "Error desconocido",
                    onRetry = { viewModel.retry() },
                    screenConfig = screenConfig
                )
            }
            uiState.notifications.isEmpty() -> {
                EmptyNotificationsScreen(screenConfig)
            }
            else -> {
                LazyColumn(
                    contentPadding = PaddingValues(screenConfig.padding),
                    verticalArrangement = Arrangement.spacedBy(screenConfig.spacing)
                ) {
                    items(uiState.notifications) { notification ->
                        EnhancedNotificationCard(
                            notification = notification,
                            screenConfig = screenConfig,
                            onMarkAsRead = {  }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EnhancedNotificationCard(
    notification: NotificationData,
    screenConfig: ScreenConfig,
    onMarkAsRead: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(screenConfig.cornerRadius))
            .clickable {
                if (!notification.isRead) {
                    onMarkAsRead()
                }
            },
        shape = RoundedCornerShape(screenConfig.cornerRadius),
        colors = CardDefaults.cardColors(
            containerColor = if (notification.isRead)
                DarkGray.copy(alpha = 0.6f) else DarkGray.copy(alpha = 0.95f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(screenConfig.cardPadding),
            horizontalArrangement = Arrangement.spacedBy(screenConfig.spacing)
        ) {
            // Imagen del producto si está disponible
            notification.product?.let { product ->
                Box(
                    modifier = Modifier
                        .size(screenConfig.iconSize * 2.5f)
                        .clip(RoundedCornerShape(screenConfig.cornerRadius / 2))
                        .background(Color.Gray.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    val imageUrl = product.image_url.firstOrNull()
                    if (!imageUrl.isNullOrEmpty()) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = product.name,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        NotificationIcon(
                            type = notification.type,
                            isImportant = notification.isImportant,
                            screenConfig = screenConfig
                        )
                    }
                }
            } ?: run {
                NotificationIcon(
                    type = notification.type,
                    isImportant = notification.isImportant,
                    screenConfig = screenConfig
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = notification.title,
                        fontSize = screenConfig.bodySize,
                        fontWeight = FontWeight.Bold,
                        color = if (notification.isRead) Color.Gray else Color.White,
                        modifier = Modifier.weight(1f)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(screenConfig.spacing / 2)
                    ) {
                        if (!notification.isRead) {
                            Box(
                                modifier = Modifier
                                    .size(screenConfig.iconSize * 0.5f)
                                    .background(PCIBlue, CircleShape)
                            )
                        }

                        if (notification.isImportant) {
                            Icon(
                                imageVector = Icons.Default.PriorityHigh,
                                contentDescription = "Importante",
                                modifier = Modifier.size(screenConfig.iconSize * 0.8f),
                                tint = Color(0xFFEF4444)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(screenConfig.spacing / 3))

                Text(
                    text = notification.message,
                    fontSize = screenConfig.captionSize,
                    color = if (notification.isRead) Color.Gray else Color(0xFFD1D5DB),
                    lineHeight = (screenConfig.captionSize.value * 1.4f).sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                // Mostrar información del producto si está disponible
                notification.product?.let { product ->
                    Spacer(modifier = Modifier.height(screenConfig.spacing / 2))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "$${product.price}",
                            fontSize = screenConfig.captionSize,
                            color = PCIBlue,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Stock: ${product.stock}",
                            fontSize = screenConfig.captionSize * 0.9f,
                            color = if (product.stock > 0) Color(0xFF10B981) else Color(0xFFEF4444),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(screenConfig.spacing / 2))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = notification.time,
                        fontSize = screenConfig.captionSize * 0.9f,
                        color = Color(0xFF9CA3AF)
                    )

                    if (notification.isImportant) {
                        NotificationPriorityBadge(
                            isImportant = notification.isImportant,
                            screenConfig = screenConfig
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationIcon(type: String, isImportant: Boolean, screenConfig: ScreenConfig) {
    val (icon, color) = when (type) {
        "promo" -> Icons.Default.LocalOffer to Color(0xFFF59E0B)
        "stock" -> Icons.Default.Inventory to Color(0xFF10B981)
        "reminder" -> Icons.Default.Schedule to Color(0xFF3B82F6)
        "delivery" -> Icons.Default.LocalShipping to Color(0xFF8B5CF6)
        "offer" -> Icons.Default.Diamond to Color(0xFFEC4899)
        else -> Icons.Default.Notifications to Color.Gray
    }

    Box(
        modifier = Modifier
            .size(screenConfig.iconSize * 2f)
            .background(
                color.copy(alpha = if (isImportant) 0.3f else 0.2f),
                CircleShape
            )
            .border(
                1.dp,
                color.copy(alpha = if (isImportant) 0.6f else 0.4f),
                CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(screenConfig.iconSize),
            tint = color
        )
    }
}

@Composable
fun NotificationPriorityBadge(isImportant: Boolean, screenConfig: ScreenConfig) {
    if (isImportant) {
        Box(
            modifier = Modifier
                .background(
                    Color(0xFFEF4444).copy(alpha = 0.2f),
                    RoundedCornerShape(screenConfig.cornerRadius / 3)
                )
                .border(
                    1.dp,
                    Color(0xFFEF4444).copy(alpha = 0.5f),
                    RoundedCornerShape(screenConfig.cornerRadius / 3)
                )
                .padding(horizontal = screenConfig.spacing / 2, vertical = screenConfig.spacing / 4)
        ) {
            Text(
                text = "Urgente",
                fontSize = screenConfig.captionSize * 0.8f,
                color = Color(0xFFEF4444),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun LoadingNotificationsScreen(screenConfig: ScreenConfig) {
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
                text = "Cargando notificaciones...",
                fontSize = screenConfig.bodySize,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ErrorNotificationsScreen(
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
                text = "Error al cargar notificaciones",
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
                overflow = TextOverflow.Ellipsis
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

@Composable
fun EmptyNotificationsScreen(screenConfig: ScreenConfig) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(screenConfig.spacing)
        ) {
            Icon(
                imageVector = Icons.Default.NotificationsNone,
                contentDescription = null,
                modifier = Modifier.size(screenConfig.iconSize * 3f),
                tint = Color.Gray
            )

            Text(
                text = "Sin notificaciones",
                fontSize = screenConfig.bodySize,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = "No tienes notificaciones nuevas en este momento",
                fontSize = screenConfig.captionSize,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                lineHeight = (screenConfig.captionSize.value * 1.3f).sp
            )
        }
    }
}
