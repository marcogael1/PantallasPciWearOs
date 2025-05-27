package com.example.plantillasproyecto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PCIStoreTheme {
                PCIStoreApp()
            }
        }
    }
}

// ✅ Configuración adaptable basada en el tamaño de pantalla
@Composable
fun rememberScreenConfig(): ScreenConfig {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    return when {
        screenWidth <= 320.dp -> ScreenConfig.Small // Pantallas pequeñas (320x320)
        screenWidth <= 390.dp -> ScreenConfig.Medium // Pantallas medianas (390x390)
        else -> ScreenConfig.Large // Pantallas grandes (454x454+)
    }
}

enum class ScreenConfig(
    val padding: Dp,
    val cardPadding: Dp,
    val buttonHeight: Dp,
    val iconSize: Dp,
    val titleSize: androidx.compose.ui.unit.TextUnit,
    val bodySize: androidx.compose.ui.unit.TextUnit,
    val captionSize: androidx.compose.ui.unit.TextUnit,
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

@Composable
fun PCIStoreApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") { HomeScreen(navController) }
        composable("orders") { OrdersScreen(navController) }
        composable("notifications") { NotificationsScreen(navController) }
        composable("voice") { VoiceScreen(navController) }
        composable("favorites") { FavoritesScreen(navController) }
        composable("stores") { StoresScreen(navController) }
    }
}

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
                // Header adaptable
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
        // Primera fila
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

        // Segunda fila
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

        // Botón de tiendas adaptable
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
            kotlinx.coroutines.delay(100)
            isPressed = false
        }
    }
}

@Composable
fun OrdersScreen(navController: NavHostController) {
    val screenConfig = rememberScreenConfig()

    ScreenWithHeader(
        title = "Mis Pedidos",
        screenConfig = screenConfig,
        onBackClick = { navController.popBackStack() }
    ) {
        val orders = listOf(
            OrderData("PCI-001", "delivered", "Laptop Gaming ROG", "Hoy 14:30", "$2,499"),
            OrderData("PCI-002", "transit", "Mouse Logitech MX", "Ayer 09:15", "$89"),
            OrderData("PCI-003", "processing", "Teclado Mecánico RGB", "2 días", "$159"),
            OrderData("PCI-004", "cancelled", "Monitor 4K Samsung", "3 días", "$599")
        )

        LazyColumn(
            contentPadding = PaddingValues(screenConfig.padding),
            verticalArrangement = Arrangement.spacedBy(screenConfig.spacing)
        ) {
            items(orders) { order ->
                EnhancedOrderCard(order = order, screenConfig = screenConfig)
            }
        }
    }
}

@Composable
fun EnhancedOrderCard(order: OrderData, screenConfig: ScreenConfig) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(screenConfig.cornerRadius)),
        shape = RoundedCornerShape(screenConfig.cornerRadius),
        colors = CardDefaults.cardColors(
            containerColor = DarkGray
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(screenConfig.cardPadding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(screenConfig.spacing / 2)
                ) {
                    Text(
                        text = order.id,
                        fontSize = screenConfig.bodySize,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    StatusBadge(status = order.status, screenConfig = screenConfig)
                }

                Spacer(modifier = Modifier.height(screenConfig.spacing / 2))

                Text(
                    text = order.product,
                    fontSize = screenConfig.captionSize,
                    color = Color(0xFFD1D5DB),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(screenConfig.spacing / 3))

                Text(
                    text = order.price,
                    fontSize = screenConfig.bodySize,
                    fontWeight = FontWeight.Bold,
                    color = PCIBlue
                )
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(screenConfig.spacing / 3)
            ) {
                StatusIcon(status = order.status, screenConfig = screenConfig)

                Text(
                    text = order.date,
                    fontSize = screenConfig.captionSize * 0.8f,
                    color = Color(0xFF9CA3AF),
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

@Composable
fun StatusBadge(status: String, screenConfig: ScreenConfig) {
    val (color, text) = when (status) {
        "delivered" -> Color(0xFF10B981) to "Entregado"
        "transit" -> Color(0xFFF59E0B) to "En tránsito"
        "processing" -> Color(0xFF3B82F6) to "Procesando"
        "cancelled" -> Color(0xFFEF4444) to "Cancelado"
        else -> Color.Gray to "Desconocido"
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
fun StatusIcon(status: String, screenConfig: ScreenConfig) {
    val (icon, color) = when (status) {
        "delivered" -> Icons.Default.CheckCircle to Color(0xFF10B981)
        "transit" -> Icons.Default.LocalShipping to Color(0xFFF59E0B)
        "processing" -> Icons.Default.Schedule to Color(0xFF3B82F6)
        "cancelled" -> Icons.Default.Cancel to Color(0xFFEF4444)
        else -> Icons.Default.Help to Color.Gray
    }

    Box(
        modifier = Modifier
            .size(screenConfig.iconSize * 1.5f)
            .background(color.copy(alpha = 0.2f), CircleShape)
            .border(1.dp, color.copy(alpha = 0.5f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(screenConfig.iconSize * 0.8f),
            tint = color
        )
    }
}

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
            // Header adaptable
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

// Implementación similar para otras pantallas...
@Composable
fun NotificationsScreen(navController: NavHostController) {
    val screenConfig = rememberScreenConfig()

    ScreenWithHeader(
        title = "Notificaciones",
        screenConfig = screenConfig,
        onBackClick = { navController.popBackStack() }
    ) {
        val notifications = listOf(
            NotificationData("promo", "🔥 50% OFF", "Laptops Gaming hasta agotar stock", "Ahora", true),
            NotificationData("stock", "📦 Disponible", "iPhone 15 Pro Max ya llegó", "5 min", true),
            NotificationData("reminder", "⏰ Recordatorio", "Cables USB-C en tu lista", "1 hora", false),
            NotificationData("delivery", "🚚 Entrega", "Tu pedido llegará hoy", "2 horas", true),
            NotificationData("offer", "💎 Oferta", "AirPods Pro con 30% descuento", "3 horas", false)
        )

        LazyColumn(
            contentPadding = PaddingValues(screenConfig.padding),
            verticalArrangement = Arrangement.spacedBy(screenConfig.spacing)
        ) {
            items(notifications) { notification ->
                EnhancedNotificationCard(notification = notification, screenConfig = screenConfig)
            }
        }
    }
}

@Composable
fun EnhancedNotificationCard(notification: NotificationData, screenConfig: ScreenConfig) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(screenConfig.cornerRadius)),
        shape = RoundedCornerShape(screenConfig.cornerRadius),
        colors = CardDefaults.cardColors(
            containerColor = if (notification.isImportant)
                DarkGray.copy(alpha = 0.95f) else DarkGray.copy(alpha = 0.8f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(screenConfig.cardPadding),
            horizontalArrangement = Arrangement.spacedBy(screenConfig.spacing)
        ) {
            NotificationIcon(type = notification.type, isImportant = notification.isImportant, screenConfig = screenConfig)

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
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )

                    if (notification.isImportant) {
                        Box(
                            modifier = Modifier
                                .size(screenConfig.iconSize * 0.5f)
                                .background(Color(0xFFEF4444), CircleShape)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(screenConfig.spacing / 3))

                Text(
                    text = notification.message,
                    fontSize = screenConfig.captionSize,
                    color = Color(0xFFD1D5DB),
                    lineHeight = (screenConfig.captionSize.value * 1.4f).sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

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

                    NotificationPriorityBadge(
                        isImportant = notification.isImportant,
                        screenConfig = screenConfig
                    )
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
fun VoiceScreen(navController: NavHostController) {
    val screenConfig = rememberScreenConfig()

    ScreenWithHeader(
        title = "Búsqueda por Voz",
        screenConfig = screenConfig,
        onBackClick = { navController.popBackStack() }
    ) {
        var isListening by remember { mutableStateOf(false) }
        var searchQuery by remember { mutableStateOf("") }

        val infiniteTransition = rememberInfiniteTransition(label = "pulse")

        val pulseScale by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = if (isListening) 1.15f else 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulse"
        )

        val pulseAlpha by infiniteTransition.animateFloat(
            initialValue = 0.3f,
            targetValue = if (isListening) 0.8f else 0.3f,
            animationSpec = infiniteRepeatable(
                animation = tween(800),
                repeatMode = RepeatMode.Reverse
            ),
            label = "alpha"
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(screenConfig.padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Círculos de pulso animados
            Box(
                contentAlignment = Alignment.Center
            ) {
                // Círculo exterior animado
                if (isListening) {
                    Box(
                        modifier = Modifier
                            .size((screenConfig.buttonHeight * 2f * pulseScale))
                            .background(
                                Color(0xFFEF4444).copy(alpha = pulseAlpha * 0.3f),
                                CircleShape
                            )
                    )
                }

                // Círculo medio
                if (isListening) {
                    Box(
                        modifier = Modifier
                            .size(screenConfig.buttonHeight * 1.5f)
                            .background(
                                Color(0xFFEF4444).copy(alpha = 0.4f),
                                CircleShape
                            )
                    )
                }

                // Botón principal
                Box(
                    modifier = Modifier
                        .size(screenConfig.buttonHeight * 1.2f)
                        .shadow(12.dp, CircleShape)
                        .background(
                            if (isListening)
                                Brush.radialGradient(
                                    colors = listOf(Color(0xFFEF4444), Color(0xFFDC2626))
                                ) else
                                Brush.radialGradient(
                                    colors = listOf(PCIBlue, Color(0xFF1E40AF))
                                ),
                            CircleShape
                        )
                        .clickable {
                            isListening = !isListening
                            if (isListening) {
                                searchQuery = ""
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isListening) Icons.Default.Stop else Icons.Default.Mic,
                        contentDescription = null,
                        modifier = Modifier.size(screenConfig.iconSize * 1.5f),
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(screenConfig.spacing * 2))

            // Estado y texto
            Text(
                text = when {
                    isListening -> "🎤 Escuchando..."
                    searchQuery.isNotEmpty() -> "Buscando: \"$searchQuery\""
                    else -> "Toca para buscar por voz"
                },
                fontSize = screenConfig.bodySize,
                color = when {
                    isListening -> Color(0xFFEF4444)
                    searchQuery.isNotEmpty() -> PCIBlue
                    else -> Color(0xFF9CA3AF)
                },
                textAlign = TextAlign.Center,
                fontWeight = if (isListening) FontWeight.Medium else FontWeight.Normal
            )

            Spacer(modifier = Modifier.height(screenConfig.spacing))

            // Instrucciones
            Text(
                text = when {
                    isListening -> "Di el nombre del producto que buscas..."
                    searchQuery.isNotEmpty() -> "Resultados encontrados"
                    else -> "Busca productos, marcas o categorías"
                },
                fontSize = screenConfig.captionSize,
                color = Color(0xFF6B7280),
                textAlign = TextAlign.Center,
                lineHeight = (screenConfig.captionSize.value * 1.3f).sp
            )

            // Simulación de resultados de búsqueda
            if (searchQuery.isNotEmpty()) {
                Spacer(modifier = Modifier.height(screenConfig.spacing * 2))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(screenConfig.spacing / 2)
                ) {
                    items(3) { index ->
                        VoiceSearchResultCard(
                            productName = when (index) {
                                0 -> "iPhone 15 Pro"
                                1 -> "MacBook Air M2"
                                else -> "AirPods Pro"
                            },
                            price = when (index) {
                                0 -> "$999"
                                1 -> "$1,199"
                                else -> "$249"
                            },
                            screenConfig = screenConfig
                        )
                    }
                }
            }
        }

        // Simular búsqueda después de escuchar
        LaunchedEffect(isListening) {
            if (isListening) {
                kotlinx.coroutines.delay(3000)
                isListening = false
                searchQuery = "iPhone"
            }
        }
    }
}

@Composable
fun VoiceSearchResultCard(productName: String, price: String, screenConfig: ScreenConfig) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(screenConfig.cornerRadius)),
        shape = RoundedCornerShape(screenConfig.cornerRadius),
        colors = CardDefaults.cardColors(containerColor = DarkGray.copy(alpha = 0.8f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(screenConfig.cardPadding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = productName,
                    fontSize = screenConfig.captionSize,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
                Text(
                    text = price,
                    fontSize = screenConfig.captionSize * 0.9f,
                    color = PCIBlue,
                    fontWeight = FontWeight.Bold
                )
            }

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                modifier = Modifier.size(screenConfig.iconSize * 0.8f),
                tint = Color(0xFF9CA3AF)
            )
        }
    }
}

@Composable
fun FavoritesScreen(navController: NavHostController) {
    val screenConfig = rememberScreenConfig()

    ScreenWithHeader(
        title = "Favoritos",
        screenConfig = screenConfig,
        onBackClick = { navController.popBackStack() }
    ) {
        val favorites = listOf(
            FavoriteData("iPad Pro 12.9\"", "$1,099", true, 4.8f),
            FavoriteData("AirPods Pro 2", "$249", false, 4.9f),
            FavoriteData("Apple Watch Ultra", "$799", true, 4.7f),
            FavoriteData("MacBook Air M2", "$1,199", true, 4.9f),
            FavoriteData("iPhone 15 Pro Max", "$1,199", false, 4.8f)
        )

        LazyColumn(
            contentPadding = PaddingValues(screenConfig.padding),
            verticalArrangement = Arrangement.spacedBy(screenConfig.spacing)
        ) {
            items(favorites) { favorite ->
                EnhancedFavoriteCard(favorite = favorite, screenConfig = screenConfig)
            }
        }
    }
}

@Composable
fun EnhancedFavoriteCard(favorite: FavoriteData, screenConfig: ScreenConfig) {
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
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(screenConfig.spacing / 2)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        modifier = Modifier.size(screenConfig.iconSize * 0.8f),
                        tint = Color(0xFFEF4444)
                    )

                    Text(
                        text = favorite.name,
                        fontSize = screenConfig.bodySize,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(screenConfig.spacing / 2))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(screenConfig.spacing)
                ) {
                    Text(
                        text = favorite.price,
                        fontSize = screenConfig.bodySize,
                        fontWeight = FontWeight.Bold,
                        color = PCIBlue
                    )

                    RatingDisplay(rating = favorite.rating, screenConfig = screenConfig)
                }

                Spacer(modifier = Modifier.height(screenConfig.spacing / 2))

                AvailabilityBadge(isAvailable = favorite.available, screenConfig = screenConfig)
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(screenConfig.spacing / 2)
            ) {
                // Botón de carrito
                Box(
                    modifier = Modifier
                        .size(screenConfig.iconSize * 2f)
                        .background(
                            if (favorite.available) PCIBlue.copy(alpha = 0.2f) else Color.Gray.copy(alpha = 0.2f),
                            RoundedCornerShape(screenConfig.cornerRadius / 2)
                        )
                        .border(
                            1.dp,
                            if (favorite.available) PCIBlue.copy(alpha = 0.5f) else Color.Gray.copy(alpha = 0.5f),
                            RoundedCornerShape(screenConfig.cornerRadius / 2)
                        )
                        .clickable(enabled = favorite.available) { },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(screenConfig.iconSize),
                        tint = if (favorite.available) PCIBlue else Color.Gray
                    )
                }

                // Botón de eliminar favorito
                Box(
                    modifier = Modifier
                        .size(screenConfig.iconSize * 1.5f)
                        .background(
                            Color(0xFFEF4444).copy(alpha = 0.2f),
                            RoundedCornerShape(screenConfig.cornerRadius / 3)
                        )
                        .border(
                            1.dp,
                            Color(0xFFEF4444).copy(alpha = 0.5f),
                            RoundedCornerShape(screenConfig.cornerRadius / 3)
                        )
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        modifier = Modifier.size(screenConfig.iconSize * 0.7f),
                        tint = Color(0xFFEF4444)
                    )
                }
            }
        }
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

@Composable
fun AvailabilityBadge(isAvailable: Boolean, screenConfig: ScreenConfig) {
    val (color, text, icon) = if (isAvailable) {
        Triple(Color(0xFF10B981), "Disponible", Icons.Default.CheckCircle)
    } else {
        Triple(Color(0xFFEF4444), "Agotado", Icons.Default.Cancel)
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
            text = text,
            fontSize = screenConfig.captionSize * 0.9f,
            color = color,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun StoresScreen(navController: NavHostController) {
    val screenConfig = rememberScreenConfig()

    ScreenWithHeader(
        title = "Tiendas Cercanas",
        screenConfig = screenConfig,
        onBackClick = { navController.popBackStack() }
    ) {
        val stores = listOf(
            StoreData("PCI Centro", "0.5 km", "9:00 - 21:00", "Abierto", 4.8f),
            StoreData("PCI Mall Plaza", "1.2 km", "10:00 - 22:00", "Abierto", 4.6f),
            StoreData("PCI Norte", "2.8 km", "9:00 - 20:00", "Cerrado", 4.7f),
            StoreData("PCI Express", "3.1 km", "24 horas", "Abierto", 4.5f)
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
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Navigation,
                    contentDescription = null,
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

// Data Classes
data class OrderData(
    val id: String,
    val status: String,
    val product: String,
    val date: String,
    val price: String
)

data class NotificationData(
    val type: String,
    val title: String,
    val message: String,
    val time: String,
    val isImportant: Boolean
)

data class FavoriteData(
    val name: String,
    val price: String,
    val available: Boolean,
    val rating: Float
)

data class StoreData(
    val name: String,
    val distance: String,
    val hours: String,
    val status: String,
    val rating: Float
)

// Colors
val PCIBlue = Color(0xFF0057D9)
val DarkGray = Color(0xFF374151)

// ✅ PREVIEWS PARA DIFERENTES TAMAÑOS DE PANTALLA

@Preview(
    name = "Pantalla Pequeña 320x320",
    device = "spec:width=320dp,height=320dp,dpi=240,isRound=true,chinSize=0dp,orientation=portrait",
    showBackground = true,
    backgroundColor = 0xFF000000
)
@Composable
fun HomeScreenSmallPreview() {
    PCIStoreTheme {
        HomeScreen(rememberNavController())
    }
}

@Preview(
    name = "Pantalla Mediana 390x390",
    device = "spec:width=390dp,height=390dp,dpi=240,isRound=true,chinSize=0dp,orientation=portrait",
    showBackground = true,
    backgroundColor = 0xFF000000
)
@Composable
fun HomeScreenMediumPreview() {
    PCIStoreTheme {
        HomeScreen(rememberNavController())
    }
}

@Preview(
    name = "Pantalla Grande 454x454",
    device = "spec:width=454dp,height=454dp,dpi=240,isRound=true,chinSize=0dp,orientation=portrait",
    showBackground = true,
    backgroundColor = 0xFF000000
)
@Composable
fun HomeScreenLargePreview() {
    PCIStoreTheme {
        HomeScreen(rememberNavController())
    }
}

@Preview(
    name = "Galaxy Watch 4",
    device = Devices.WEAR_OS_LARGE_ROUND,
    showBackground = true,
    backgroundColor = 0xFF000000
)
@Composable
fun HomeScreenGalaxyWatch4Preview() {
    PCIStoreTheme {
        HomeScreen(rememberNavController())
    }
}

@Preview(
    name = "Apple Watch Series 7",
    device = "spec:width=396dp,height=484dp,dpi=326,isRound=false,chinSize=0dp,orientation=portrait",
    showBackground = true,
    backgroundColor = 0xFF000000
)
@Composable
fun HomeScreenAppleWatchPreview() {
    PCIStoreTheme {
        HomeScreen(rememberNavController())
    }
}

@Preview(
    name = "Pedidos - Pantalla Pequeña",
    device = "spec:width=320dp,height=320dp,dpi=240,isRound=true,chinSize=0dp,orientation=portrait",
    showBackground = true,
    backgroundColor = 0xFF000000
)
@Composable
fun OrdersScreenSmallPreview() {
    PCIStoreTheme {
        OrdersScreen(rememberNavController())
    }
}

@Preview(
    name = "Pedidos - Pantalla Grande",
    device = "spec:width=454dp,height=454dp,dpi=240,isRound=true,chinSize=0dp,orientation=portrait",
    showBackground = true,
    backgroundColor = 0xFF000000
)
@Composable
fun OrdersScreenLargePreview() {
    PCIStoreTheme {
        OrdersScreen(rememberNavController())
    }
}

@Preview(
    name = "Notificaciones - Pantalla Pequeña",
    device = "spec:width=320dp,height=320dp,dpi=240,isRound=true,chinSize=0dp,orientation=portrait",
    showBackground = true,
    backgroundColor = 0xFF000000
)
@Composable
fun NotificationsScreenSmallPreview() {
    PCIStoreTheme {
        NotificationsScreen(rememberNavController())
    }
}

@Preview(
    name = "Búsqueda por Voz - Pantalla Grande",
    device = "spec:width=454dp,height=454dp,dpi=240,isRound=true,chinSize=0dp,orientation=portrait",
    showBackground = true,
    backgroundColor = 0xFF000000
)
@Composable
fun VoiceScreenLargePreview() {
    PCIStoreTheme {
        VoiceScreen(rememberNavController())
    }
}

@Preview(
    name = "Favoritos - Pantalla Mediana",
    device = "spec:width=390dp,height=390dp,dpi=240,isRound=true,chinSize=0dp,orientation=portrait",
    showBackground = true,
    backgroundColor = 0xFF000000
)
@Composable
fun FavoritesScreenMediumPreview() {
    PCIStoreTheme {
        FavoritesScreen(rememberNavController())
    }
}

@Preview(
    name = "Tiendas - Pantalla Grande",
    device = "spec:width=454dp,height=454dp,dpi=240,isRound=true,chinSize=0dp,orientation=portrait",
    showBackground = true,
    backgroundColor = 0xFF000000
)
@Composable
fun StoresScreenLargePreview() {
    PCIStoreTheme {
        StoresScreen(rememberNavController())
    }
}
