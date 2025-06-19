package com.example.plantillasproyecto.data.api

import com.example.plantillasproyecto.data.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface FavoritesApiService {
    @GET("cart/user/{userId}")
    suspend fun getUserFavorites(@Path("userId") userId: Int): Response<List<FavoriteProduct>>
}

interface OrdersApiService {
    @GET("cart/order/{userId}")
    suspend fun getUserOrders(@Path("userId") userId: Int): Response<List<Order>>
}

interface ProductsApiService {
    @GET("products")
    suspend fun getAllProducts(): Response<List<Product>>
}

// Nueva interfaz para notificaciones
interface NotificationsApiService {
    @GET("cart/notification/{userId}")
    suspend fun getUserNotifications(@Path("userId") userId: Int): Response<List<NotificationResponse>>
}
