package com.example.plantillasproyecto.data

data class NotificationResponse(
    val id: Int,
    val title: String,
    val message: String,
    val isRead: Boolean,
    val createdAt: String,
    val product: NotificationProduct
)

data class NotificationProduct(
    val id: Int,
    val name: String,
    val price: String,
    val stock: Int,
    val category_id: Int,
    val brand_id: Int,
    val color_id: Int,
    val image_url: List<String>,
    val created_at: String,
    val updated_at: String
)

// Modelo interno para la UI
data class NotificationData(
    val id: Int,
    val type: String,
    val title: String,
    val message: String,
    val time: String,
    val isImportant: Boolean,
    val isRead: Boolean,
    val product: NotificationProduct?
)


// API Data Classes para Favoritos
data class FavoriteProduct(
    val productId: Int,
    val productName: String,
    val price: String,
    val imageUrl: List<String>
)

// API Data Classes para Orders
data class ShippingAddress(
    val id: Int,
    val estado: String,
    val municipio: String,
    val colonia: String,
    val calle: String,
    val numero: String?,
    val codigo_postal: String,
    val referencias: String
)

data class OrderItem(
    val productId: Int,
    val productName: String,
    val price: String,
    val quantity: Int,
    val imageUrl: List<String>
)

data class Order(
    val orderId: Int,
    val status: String,
    val totalAmount: String,
    val createdAt: String,
    val shippingAddress: ShippingAddress,
    val items: List<OrderItem>
)

// Data Classes para Productos
data class Product(
    val id: Int,
    val name: String,
    val price: String,
    val stock: Int,
    val category_id: Int,
    val brand_id: Int,
    val color_id: Int,
    val image_url: List<String>,
    val created_at: String,
    val updated_at: String
)

// UI Data Classes
data class OrderData(
    val id: String,
    val status: String,
    val product: String,
    val date: String,
    val price: String
)

data class StoreData(
    val name: String,
    val distance: String,
    val hours: String,
    val status: String,
    val rating: Float,
    val latitude: Double,
    val longitude: Double
)