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
import com.example.plantillasproyecto.data.FavoriteProduct
import com.example.plantillasproyecto.ui.components.*
import com.example.plantillasproyecto.ui.theme.*
import com.example.plantillasproyecto.ui.viewmodels.FavoritesViewModel

@Composable
fun FavoritesScreen(
    navController: NavHostController,
    viewModel: FavoritesViewModel = viewModel()
) {
    val screenConfig = rememberScreenConfig()
    val uiState by viewModel.uiState.collectAsState()

    ScreenWithHeader(
        title = "Favoritos",
        screenConfig = screenConfig,
        onBackClick = { navController.popBackStack() }
    ) {
        when {
            uiState.isLoading -> {
                LoadingScreen(screenConfig)
            }
            uiState.error != null -> {
                val errorMessage = uiState.error ?: "Error desconocido"
                ErrorScreen(
                    error = errorMessage,
                    onRetry = { viewModel.retry() },
                    screenConfig = screenConfig
                )
            }
            uiState.favorites.isEmpty() -> {
                EmptyFavoritesScreen(screenConfig)
            }
            else -> {
                LazyColumn(
                    contentPadding = PaddingValues(screenConfig.padding),
                    verticalArrangement = Arrangement.spacedBy(screenConfig.spacing)
                ) {
                    items(uiState.favorites) { favorite ->
                        FavoriteProductCard(
                            product = favorite,
                            screenConfig = screenConfig
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FavoriteProductCard(
    product: FavoriteProduct,
    screenConfig: ScreenConfig
) {
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
            horizontalArrangement = Arrangement.spacedBy(screenConfig.spacing)
        ) {
            Box(
                modifier = Modifier
                    .size(screenConfig.iconSize * 3f)
                    .clip(RoundedCornerShape(screenConfig.cornerRadius / 2))
                    .background(Color.Gray.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                val imageUrl = product.imageUrl.firstOrNull()

                if (!imageUrl.isNullOrEmpty()) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = product.productName,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        onError = {
                            println("Error cargando imagen: $imageUrl")
                        }
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.ShoppingBag,
                        contentDescription = null,
                        tint = PCIBlue.copy(alpha = 0.7f),
                        modifier = Modifier.size(screenConfig.iconSize)
                    )
                }
            }

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
                }

                Spacer(modifier = Modifier.height(screenConfig.spacing / 3))

                Text(
                    text = product.productName.trim(),
                    fontSize = screenConfig.captionSize,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(screenConfig.spacing / 2))

                Text(
                    text = "$${product.price}",
                    fontSize = screenConfig.bodySize,
                    fontWeight = FontWeight.Bold,
                    color = PCIBlue
                )

                Spacer(modifier = Modifier.height(screenConfig.spacing / 2))

                Box(
                    modifier = Modifier
                        .background(
                            PCIBlue.copy(alpha = 0.2f),
                            RoundedCornerShape(screenConfig.cornerRadius / 2)
                        )
                        .border(
                            1.dp,
                            PCIBlue.copy(alpha = 0.5f),
                            RoundedCornerShape(screenConfig.cornerRadius / 2)
                        )
                        .clickable { /* Agregar al carrito */ }
                        .padding(horizontal = screenConfig.spacing, vertical = screenConfig.spacing / 2)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(screenConfig.spacing / 3)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = null,
                            modifier = Modifier.size(screenConfig.iconSize * 0.7f),
                            tint = PCIBlue
                        )
                        Text(
                            text = "Agregar",
                            fontSize = screenConfig.captionSize * 0.9f,
                            color = PCIBlue,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingScreen(screenConfig: ScreenConfig) {
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
                text = "Cargando favoritos...",
                fontSize = screenConfig.bodySize,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ErrorScreen(
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
                text = "Error al cargar favoritos",
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
fun EmptyFavoritesScreen(screenConfig: ScreenConfig) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(screenConfig.spacing)
        ) {
            Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = null,
                modifier = Modifier.size(screenConfig.iconSize * 3f),
                tint = Color.Gray
            )

            Text(
                text = "Sin favoritos",
                fontSize = screenConfig.bodySize,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Agrega productos a tus favoritos para verlos aquí",
                fontSize = screenConfig.captionSize,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                lineHeight = (screenConfig.captionSize.value * 1.3f).sp
            )
        }
    }
}