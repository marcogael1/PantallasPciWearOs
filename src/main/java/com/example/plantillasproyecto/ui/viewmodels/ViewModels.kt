package com.example.plantillasproyecto.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantillasproyecto.data.*
import com.example.plantillasproyecto.data.api.*
import com.example.plantillasproyecto.data.repositories.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// ViewModel para Notificaciones
data class NotificationsUiState(
    val notifications: List<NotificationData> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class NotificationsViewModel : ViewModel() {

    private val apiService: NotificationsApiService by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/") // Para emulador Android
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NotificationsApiService::class.java)
    }

    private val repository = NotificationsRepository(apiService)

    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()

    private val userId = 6 // ID del usuario hardcodeado

    init {
        loadNotifications()
    }

    fun loadNotifications() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                repository.getNotifications(userId).collect { notifications ->
                    _uiState.value = _uiState.value.copy(
                        notifications = notifications,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error desconocido"
                )
            }
        }
    }



    fun retry() {
        loadNotifications()
    }
}

// ViewModels existentes actualizados
data class VoiceSearchUiState(
    val isListening: Boolean = false,
    val searchQuery: String = "",
    val searchResults: List<Product> = emptyList(),
    val allProducts: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val hasSearched: Boolean = false,
    val hasPermission: Boolean = false,
    val microphoneAvailable: Boolean = false
)

class VoiceSearchViewModel : ViewModel() {

    private val apiService: ProductsApiService by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductsApiService::class.java)
    }

    private val repository = ProductRepository(apiService)

    private val _uiState = MutableStateFlow(VoiceSearchUiState())
    val uiState: StateFlow<VoiceSearchUiState> = _uiState.asStateFlow()

    init {
        loadAllProducts()
    }

    private fun loadAllProducts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                repository.getAllProducts().collect { products ->
                    _uiState.value = _uiState.value.copy(
                        allProducts = products,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error cargando productos"
                )
            }
        }
    }

    fun checkMicrophoneStatus(context: android.content.Context) {
        // Implementar lógica de verificación de micrófono
    }

    fun startListening() {
        _uiState.value = _uiState.value.copy(isListening = true, error = null)
    }

    fun stopListening() {
        _uiState.value = _uiState.value.copy(isListening = false)
    }

    fun onSpeechResult(spokenText: String) {
        _uiState.value = _uiState.value.copy(
            isListening = false,
            searchQuery = spokenText,
            hasSearched = true
        )
        searchProducts(spokenText)
    }

    fun onSpeechError(error: String) {
        _uiState.value = _uiState.value.copy(
            isListening = false,
            error = error
        )
    }

    private fun searchProducts(query: String) {
        viewModelScope.launch {
            try {
                repository.searchProducts(query).collect { products ->
                    _uiState.value = _uiState.value.copy(
                        searchResults = products,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Error en búsqueda"
                )
            }
        }
    }

    fun clearSearch() {
        _uiState.value = _uiState.value.copy(
            searchQuery = "",
            searchResults = emptyList(),
            hasSearched = false,
            error = null
        )
    }

    fun retry() {
        loadAllProducts()
    }
}

data class OrdersUiState(
    val orders: List<Order> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class OrdersViewModel : ViewModel() {

    private val apiService: OrdersApiService by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OrdersApiService::class.java)
    }

    private val repository = OrdersRepository(apiService)

    private val _uiState = MutableStateFlow(OrdersUiState())
    val uiState: StateFlow<OrdersUiState> = _uiState.asStateFlow()

    private val userId = 6

    init {
        loadOrders()
    }

    private fun loadOrders() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                repository.getOrders(userId).collect { orders ->
                    _uiState.value = _uiState.value.copy(
                        orders = orders,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error cargando pedidos"
                )
            }
        }
    }

    fun retry() {
        loadOrders()
    }
}

data class FavoritesUiState(
    val favorites: List<FavoriteProduct> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class FavoritesViewModel : ViewModel() {

    private val apiService: FavoritesApiService by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FavoritesApiService::class.java)
    }

    private val repository = FavoritesRepository(apiService)

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    private val userId = 6

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                repository.getFavorites(userId).collect { favorites ->
                    _uiState.value = _uiState.value.copy(
                        favorites = favorites,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error cargando favoritos"
                )
            }
        }
    }

    fun retry() {
        loadFavorites()
    }
}
