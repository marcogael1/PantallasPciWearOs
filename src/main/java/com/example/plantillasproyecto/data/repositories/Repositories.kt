package com.example.plantillasproyecto.data.repositories

import com.example.plantillasproyecto.data.*
import com.example.plantillasproyecto.data.api.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NotificationsRepository(private val apiService: NotificationsApiService) {

    fun getNotifications(userId: Int): Flow<List<NotificationData>> = flow {
        try {
            val response = apiService.getUserNotifications(userId)
            if (response.isSuccessful) {
                val notifications = response.body() ?: emptyList()
                val mappedNotifications = notifications.map { notification ->
                    NotificationData(
                        id = notification.id,
                        type = determineNotificationType(notification.title, notification.message),
                        title = notification.title,
                        message = notification.message,
                        time = formatNotificationTime(notification.createdAt),
                        isImportant = determineImportance(notification.title, notification.message),
                        isRead = notification.isRead,
                        product = notification.product
                    )
                }
                emit(mappedNotifications)
            } else {
                throw Exception("Error ${response.code()}: ${response.message()}")
            }
        } catch (e: Exception) {
            throw e
        }
    }


    private fun determineNotificationType(title: String, message: String): String {
        return when {
            title.contains("stock", ignoreCase = true) ||
                    message.contains("stock", ignoreCase = true) -> "stock"
            title.contains("disponible", ignoreCase = true) ||
                    message.contains("disponible", ignoreCase = true) -> "promo"
            title.contains("descuento", ignoreCase = true) ||
                    message.contains("descuento", ignoreCase = true) -> "offer"
            title.contains("entrega", ignoreCase = true) ||
                    message.contains("entrega", ignoreCase = true) -> "delivery"
            else -> "reminder"
        }
    }

    private fun determineImportance(title: String, message: String): Boolean {
        val urgentKeywords = listOf("urgente", "bajo stock", "agotarse", "último", "oferta limitada")
        val text = "$title $message".lowercase()
        return urgentKeywords.any { keyword -> text.contains(keyword) }
    }

    private fun formatNotificationTime(createdAt: String): String {
        return try {
            val date = createdAt.split("T")[0]
            val parts = date.split("-")
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

            "$day $monthName"
        } catch (e: Exception) {
            "Hoy"
        }
    }
}

// Repositorios existentes actualizados para usar tu patrón
class ProductRepository(private val apiService: ProductsApiService) {
    fun getAllProducts(): Flow<List<Product>> = flow {
        try {
            val response = apiService.getAllProducts()
            if (response.isSuccessful) {
                val products = response.body() ?: emptyList()
                emit(products)
            } else {
                throw Exception("Error ${response.code()}: ${response.message()}")
            }
        } catch (e: Exception) {
            throw e
        }
    }

    fun searchProducts(query: String): Flow<List<Product>> = flow {
        try {
            // Primero obtenemos todos los productos
            val response = apiService.getAllProducts()
            if (response.isSuccessful) {
                val allProducts = response.body() ?: emptyList()
                // Filtramos localmente por el query
                val filteredProducts = allProducts.filter { product ->
                    product.name.contains(query, ignoreCase = true)
                }
                emit(filteredProducts)
            } else {
                throw Exception("Error ${response.code()}: ${response.message()}")
            }
        } catch (e: Exception) {
            throw e
        }
    }
}

class OrdersRepository(private val apiService: OrdersApiService) {
    fun getOrders(userId: Int): Flow<List<Order>> = flow {
        try {
            val response = apiService.getUserOrders(userId)
            if (response.isSuccessful) {
                val orders = response.body() ?: emptyList()
                emit(orders)
            } else {
                throw Exception("Error ${response.code()}: ${response.message()}")
            }
        } catch (e: Exception) {
            throw e
        }
    }
}

class FavoritesRepository(private val apiService: FavoritesApiService) {
    fun getFavorites(userId: Int): Flow<List<FavoriteProduct>> = flow {
        try {
            val response = apiService.getUserFavorites(userId)
            if (response.isSuccessful) {
                val favorites = response.body() ?: emptyList()
                emit(favorites)
            } else {
                throw Exception("Error ${response.code()}: ${response.message()}")
            }
        } catch (e: Exception) {
            throw e
        }
    }
}
