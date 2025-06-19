package com.example.plantillasproyecto.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.example.plantillasproyecto.data.Order
import com.example.plantillasproyecto.ui.components.*
import com.example.plantillasproyecto.ui.theme.*
import com.example.plantillasproyecto.ui.viewmodels.OrdersViewModel

@Composable
fun OrdersScreen(navController: NavHostController, viewModel: OrdersViewModel = viewModel()) {
    val screenConfig = rememberScreenConfig()
    val uiState by viewModel.uiState.collectAsState()

    ScreenWithHeader(
        title = "Mis Pedidos",
        screenConfig = screenConfig,
        onBackClick = { navController.popBackStack() }
    ) {
        when {
            uiState.isLoading -> {
                LoadingOrdersScreen(screenConfig)
            }
            uiState.error != null -> {
                val errorMessage = uiState.error ?: "Error desconocido"
                ErrorOrdersScreen(
                    error = errorMessage,
                    onRetry = { viewModel.retry() },
                    screenConfig = screenConfig
                )
            }
            uiState.orders.isEmpty() -> {
                EmptyOrdersScreen(screenConfig)
            }
            else -> {
                LazyColumn(
                    contentPadding = PaddingValues(screenConfig.padding),
                    verticalArrangement = Arrangement.spacedBy(screenConfig.spacing)
                ) {
                    items(uiState.orders) { order ->
                        OrderCard(order = order, screenConfig = screenConfig)
                    }
                }
            }
        }
    }
}

@Composable
fun OrderCard(order: Order, screenConfig: ScreenConfig) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(screenConfig.cornerRadius)),
        shape = RoundedCornerShape(screenConfig.cornerRadius),
        colors = CardDefaults.cardColors(containerColor = DarkGray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(screenConfig.cardPadding)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Pedido #${order.orderId}",
                        fontSize = screenConfig.bodySize,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = formatOrderDate(order.createdAt),
                        fontSize = screenConfig.captionSize * 0.9f,
                        color = Color(0xFF9CA3AF)
                    )
                }
                OrderStatusBadge(status = order.status, screenConfig = screenConfig)
            }

            Spacer(modifier = Modifier.height(screenConfig.spacing))

            val firstItem = order.items.firstOrNull()
            if (firstItem != null) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(screenConfig.spacing),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(screenConfig.iconSize * 2f)
                            .clip(RoundedCornerShape(screenConfig.cornerRadius / 2))
                            .background(Color.Gray.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        val imageUrl = firstItem.imageUrl.firstOrNull()
                        if (!imageUrl.isNullOrEmpty()) {
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = firstItem.productName,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.ShoppingBag,
                                contentDescription = null,
                                tint = PCIBlue.copy(alpha = 0.7f),
                                modifier = Modifier.size(screenConfig.iconSize * 0.8f)
                            )
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = firstItem.productName.trim().take(40) + if (firstItem.productName.length > 40) "..." else "",
                            fontSize = screenConfig.captionSize,
                            fontWeight = FontWeight.Medium,
                            color = Color.White,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "Cantidad: ${firstItem.quantity}",
                            fontSize = screenConfig.captionSize * 0.9f,
                            color = Color(0xFF9CA3AF)
                        )
                    }
                }

                if (order.items.size > 1) {
                    Spacer(modifier = Modifier.height(screenConfig.spacing / 2))
                    Text(
                        text = "+ ${order.items.size - 1} producto(s) más",
                        fontSize = screenConfig.captionSize * 0.8f,
                        color = PCIBlue,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(screenConfig.spacing))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = "Total: $${order.totalAmount}",
                        fontSize = screenConfig.bodySize,
                        fontWeight = FontWeight.Bold,
                        color = PCIBlue
                    )
                    Text(
                        text = "${order.shippingAddress.municipio}, ${order.shippingAddress.estado}",
                        fontSize = screenConfig.captionSize * 0.8f,
                        color = Color(0xFF9CA3AF),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                OrderStatusIcon(status = order.status, screenConfig = screenConfig)
            }
        }
    }
}

@Composable
fun OrderStatusBadge(status: String, screenConfig: ScreenConfig) {
    val (color, text) = when (status.lowercase()) {
        "entregado", "delivered" -> Color(0xFF10B981) to "Entregado"
        "en tránsito", "transit", "enviado" -> Color(0xFFF59E0B) to "En tránsito"
        "procesando", "processing", "pendiente" -> Color(0xFF3B82F6) to "Procesando"
        "cancelado", "cancelled" -> Color(0xFFEF4444) to "Cancelado"
        else -> Color.Gray to status.replaceFirstChar { it.uppercase() }
    }

    Box(
        modifier = Modifier
            .background(
                color.copy(alpha = 0.2f),
                RoundedCornerShape(screenConfig.cornerRadius / 2)
            )
            .border(1.dp, color.copy(alpha = 0.5f), RoundedCornerShape(screenConfig.cornerRadius / 2))
            .padding(horizontal = screenConfig.spacing / 2, vertical = screenConfig.spacing / 4)
    ) {
        Text(
            text = text,
            fontSize = screenConfig.captionSize * 0.8f,
            color = color,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun OrderStatusIcon(status: String, screenConfig: ScreenConfig) {
    val (icon, color) = when (status.lowercase()) {
        "entregado", "delivered" -> Icons.Default.CheckCircle to Color(0xFF10B981)
        "en tránsito", "transit", "enviado" -> Icons.Default.LocalShipping to Color(0xFFF59E0B)
        "procesando", "processing", "pendiente" -> Icons.Default.Schedule to Color(0xFF3B82F6)
        "cancelado", "cancelled" -> Icons.Default.Cancel to Color(0xFFEF4444)
        else -> Icons.Default.Help to Color.Gray
    }

    Box(
        modifier = Modifier
            .size(screenConfig.iconSize * 1.2f)
            .background(color.copy(alpha = 0.2f), CircleShape)
            .border(1.dp, color.copy(alpha = 0.5f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(screenConfig.iconSize * 0.7f),
            tint = color
        )
    }
}

@Composable
fun LoadingOrdersScreen(screenConfig: ScreenConfig) {
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
                text = "Cargando pedidos...",
                fontSize = screenConfig.bodySize,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ErrorOrdersScreen(
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
                text = "Error al cargar pedidos",
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
fun EmptyOrdersScreen(screenConfig: ScreenConfig) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(screenConfig.spacing)
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCartCheckout,
                contentDescription = null,
                modifier = Modifier.size(screenConfig.iconSize * 3f),
                tint = Color.Gray
            )

            Text(
                text = "Sin pedidos",
                fontSize = screenConfig.bodySize,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Tus pedidos aparecerán aquí una vez que realices compras",
                fontSize = screenConfig.captionSize,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                lineHeight = (screenConfig.captionSize.value * 1.3f).sp
            )
        }
    }
}

fun formatOrderDate(dateString: String): String {
    return try {
        val parts = dateString.split("T")[0].split("-")
        val year = parts[0]
        val month = parts[1]
        val day = parts[2]

        val monthName = when (month) {
            "01" -> "Ene"
            "02" -> "Feb"
            "03" -> "Mar"
            "04" -> "Abr"
            "05" -> "May"
            "06" -> "Jun"
            "07" -> "Jul"
            "08" -> "Ago"
            "09" -> "Sep"
            "10" -> "Oct"
            "11" -> "Nov"
            "12" -> "Dic"
            else -> month
        }

        "$day $monthName $year"
    } catch (e: Exception) {
        dateString
    }
}