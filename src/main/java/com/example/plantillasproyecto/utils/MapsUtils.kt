package com.example.plantillasproyecto.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.wear.remote.interactions.RemoteActivityHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object MapsUtils {
    fun openGoogleMaps(context: Context, latitude: Double, longitude: Double, label: String) {
        try {
            val mapsUrl = when (label) {
                "PCI Centro" -> "https://www.google.com.mx/maps/dir/?api=1&destination=21.1417365,-98.4200389&destination_place_id=ChIJt4eVHfUdQIYRzgOCdVOqiEE&travelmode=driving"
                "PCI Centro 2" -> "https://www.google.com.mx/maps/dir/?api=1&destination=21.142845,-98.4198247&destination_place_id=ChIJTQAAAAAAAAAAAAAAAAAAAQ&travelmode=driving"
                "PCI Norte" -> "https://www.google.com.mx/maps/dir/?api=1&destination=21.1450000,-98.4150000&travelmode=driving"
                "PCI Express" -> "https://www.google.com.mx/maps/dir/?api=1&destination=21.1400000,-98.4250000&travelmode=driving"
                else -> "https://www.google.com.mx/maps/dir/?api=1&destination=$latitude,$longitude&travelmode=driving"
            }

            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(mapsUrl)
                addCategory(Intent.CATEGORY_BROWSABLE)
            }

            Toast.makeText(context, "Iniciando navegación a $label...", Toast.LENGTH_SHORT).show()

            CoroutineScope(Dispatchers.Main).launch {
                try {
                    RemoteActivityHelper(context).startRemoteActivity(intent)
                    println("🧭 Iniciando navegación al teléfono: $label")
                } catch (e: Exception) {
                    println("❌ Error al iniciar navegación: ${e.message}")
                    Toast.makeText(context, "Error: Verifica conexión con el teléfono", Toast.LENGTH_LONG).show()
                }
            }

        } catch (e: Exception) {
            println("❌ Error al procesar navegación: ${e.message}")
            Toast.makeText(context, "Error al iniciar navegación", Toast.LENGTH_SHORT).show()
        }
    }
}