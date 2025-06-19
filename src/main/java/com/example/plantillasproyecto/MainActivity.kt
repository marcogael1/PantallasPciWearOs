package com.example.plantillasproyecto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.plantillasproyecto.ui.screens.*
import com.example.plantillasproyecto.ui.theme.PCIStoreTheme

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
        composable("voice") { VoiceSearchScreen(navController) }
        composable("favorites") { FavoritesScreen(navController) }
        composable("stores") { StoresScreen(navController) }
    }
}