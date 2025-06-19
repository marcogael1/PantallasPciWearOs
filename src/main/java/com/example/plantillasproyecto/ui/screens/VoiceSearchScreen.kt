package com.example.plantillasproyecto.ui.screens

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.plantillasproyecto.data.Product
import com.example.plantillasproyecto.ui.components.*
import com.example.plantillasproyecto.ui.theme.*
import com.example.plantillasproyecto.ui.viewmodels.VoiceSearchViewModel
import com.example.plantillasproyecto.ui.viewmodels.VoiceSearchUiState

@Composable
fun VoiceSearchScreen(
    navController: NavHostController,
    viewModel: VoiceSearchViewModel = viewModel()
) {
    val screenConfig = rememberScreenConfig()
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var permissionDenied by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.checkMicrophoneStatus(context)
        } else {
            permissionDenied = true
        }
    }

    LaunchedEffect(Unit) {
        viewModel.checkMicrophoneStatus(context)
    }

    val speechRecognizer = remember {
        if (SpeechRecognizer.isRecognitionAvailable(context)) {
            SpeechRecognizer.createSpeechRecognizer(context)
        } else {
            null
        }
    }

    val speechRecognitionListener = remember {
        object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                println("🎤 Listo para escuchar")
            }

            override fun onBeginningOfSpeech() {
                println("🎤 Comenzando a escuchar...")
            }

            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onEndOfSpeech() {
                println("🎤 Fin del habla")
            }

            override fun onError(error: Int) {
                val errorMessage = when (error) {
                    SpeechRecognizer.ERROR_AUDIO -> "Error de audio - Verifica el micrófono"
                    SpeechRecognizer.ERROR_CLIENT -> "Servicio no disponible - Configura micrófono en emulador"
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Sin permisos de micrófono"
                    SpeechRecognizer.ERROR_NETWORK -> "Sin conexión a internet"
                    SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Timeout de conexión"
                    SpeechRecognizer.ERROR_NO_MATCH -> "No se entendió el audio - Habla más claro"
                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Servicio ocupado - Intenta de nuevo"
                    SpeechRecognizer.ERROR_SERVER -> "Error del servidor de Google"
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No se detectó voz - Habla más fuerte"
                    else -> "Error desconocido ($error)"
                }

                println("❌ Error de reconocimiento: $errorMessage")
                viewModel.onSpeechError(errorMessage)
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                val spokenText = matches?.firstOrNull() ?: ""

                println("🎯 Texto reconocido: '$spokenText'")

                if (spokenText.isNotBlank()) {
                    viewModel.onSpeechResult(spokenText)
                    Toast.makeText(context, "Buscando: $spokenText", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.onSpeechError("No se pudo reconocer el audio")
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {
                val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                val partialText = matches?.firstOrNull() ?: ""
                if (partialText.isNotEmpty()) {
                    println("🔄 Resultado parcial: '$partialText'")
                }
            }

            override fun onEvent(eventType: Int, params: Bundle?) {}
        }
    }

    LaunchedEffect(speechRecognizer) {
        speechRecognizer?.setRecognitionListener(speechRecognitionListener)
    }

    DisposableEffect(Unit) {
        onDispose {
            speechRecognizer?.destroy()
        }
    }

    fun startVoiceRecognition() {
        when {
            !uiState.hasPermission -> {
                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                return
            }

            !uiState.microphoneAvailable -> {
                Toast.makeText(
                    context,
                    "Configura el micrófono en Extended Controls del emulador",
                    Toast.LENGTH_LONG
                ).show()
                return
            }

            speechRecognizer == null -> {
                Toast.makeText(
                    context,
                    "Reconocimiento de voz no disponible",
                    Toast.LENGTH_LONG
                ).show()
                return
            }
        }

        try {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-MX")
                putExtra(RecognizerIntent.EXTRA_PROMPT, "Di el producto que buscas...")
                putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
                putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
                putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 2000)
                putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 2000)
            }

            viewModel.startListening()
            speechRecognizer?.startListening(intent)
            println("🎤 Iniciando reconocimiento de voz...")

        } catch (e: Exception) {
            println("❌ Error al iniciar reconocimiento: ${e.message}")
            viewModel.onSpeechError("Error al iniciar reconocimiento: ${e.message}")
            Toast.makeText(context, "Error al iniciar micrófono", Toast.LENGTH_SHORT).show()
        }
    }

    fun stopVoiceRecognition() {
        speechRecognizer?.stopListening()
        viewModel.stopListening()
        println("🛑 Deteniendo reconocimiento de voz")
    }

    ScreenWithHeader(
        title = "Búsqueda por Voz",
        screenConfig = screenConfig,
        onBackClick = { navController.popBackStack() }
    ) {
        when {
            uiState.isLoading -> {
                LoadingProductsScreen(screenConfig)
            }

            !uiState.hasPermission && permissionDenied -> {
                PermissionDeniedScreen(
                    onRequestPermission = {
                        permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                    },
                    screenConfig = screenConfig
                )
            }

            !uiState.microphoneAvailable -> {
                MicrophoneNotAvailableScreen(screenConfig)
            }

            uiState.error != null && uiState.allProducts.isEmpty() -> {
                ErrorProductsScreen(
                    error = uiState.error ?: "Error desconocido",
                    onRetry = { viewModel.retry() },
                    screenConfig = screenConfig
                )
            }

            else -> {
                VoiceSearchContent(
                    uiState = uiState,
                    screenConfig = screenConfig,
                    onStartListening = { startVoiceRecognition() },
                    onStopListening = { stopVoiceRecognition() },
                    onClearSearch = { viewModel.clearSearch() }
                )
            }
        }
    }
}

@Composable
fun VoiceSearchContent(
    uiState: VoiceSearchUiState,
    screenConfig: ScreenConfig,
    onStartListening: () -> Unit,
    onStopListening: () -> Unit,
    onClearSearch: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(screenConfig.padding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Indicador de estado del micrófono
        if (!uiState.hasPermission || !uiState.microphoneAvailable) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = screenConfig.spacing),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF59E0B).copy(alpha = 0.2f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(screenConfig.spacing),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(screenConfig.spacing / 2)
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        modifier = Modifier.size(screenConfig.iconSize),
                        tint = Color(0xFFF59E0B)
                    )

                    Text(
                        text = when {
                            !uiState.hasPermission -> "Sin permiso de micrófono"
                            !uiState.microphoneAvailable -> "Configura micrófono en emulador"
                            else -> ""
                        },
                        fontSize = screenConfig.captionSize,
                        color = Color(0xFFF59E0B),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        VoiceMicrophoneButton(
            isListening = uiState.isListening,
            screenConfig = screenConfig,
            onStartListening = onStartListening,
            onStopListening = onStopListening,
            isEnabled = uiState.hasPermission && uiState.microphoneAvailable
        )

        Spacer(modifier = Modifier.height(screenConfig.spacing * 2))

        VoiceStatusText(uiState = uiState, screenConfig = screenConfig)

        Spacer(modifier = Modifier.height(screenConfig.spacing))

        if (uiState.hasSearched) {
            Button(
                onClick = onClearSearch,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF374151)
                ),
                modifier = Modifier.height(screenConfig.buttonHeight * 0.6f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(screenConfig.spacing / 2)
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = null,
                        modifier = Modifier.size(screenConfig.iconSize * 0.8f)
                    )
                    Text(
                        text = "Nueva búsqueda",
                        fontSize = screenConfig.captionSize
                    )
                }
            }

            Spacer(modifier = Modifier.height(screenConfig.spacing))
        }

        if (uiState.hasSearched) {
            VoiceSearchResults(
                searchResults = uiState.searchResults,
                searchQuery = uiState.searchQuery,
                screenConfig = screenConfig
            )
        }
    }
}

@Composable
fun VoiceMicrophoneButton(
    isListening: Boolean,
    screenConfig: ScreenConfig,
    onStartListening: () -> Unit,
    onStopListening: () -> Unit,
    isEnabled: Boolean = true
) {
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

    Box(contentAlignment = Alignment.Center) {
        if (isListening && isEnabled) {
            Box(
                modifier = Modifier
                    .size((screenConfig.buttonHeight * 2f * pulseScale))
                    .background(
                        Color(0xFFEF4444).copy(alpha = pulseAlpha * 0.3f),
                        CircleShape
                    )
            )

            Box(
                modifier = Modifier
                    .size(screenConfig.buttonHeight * 1.5f)
                    .background(
                        Color(0xFFEF4444).copy(alpha = 0.4f),
                        CircleShape
                    )
            )
        }

        Box(
            modifier = Modifier
                .size(screenConfig.buttonHeight * 1.2f)
                .shadow(12.dp, CircleShape)
                .background(
                    brush = when {
                        !isEnabled -> Brush.radialGradient(
                            colors = listOf(Color.Gray, Color.Gray)
                        )
                        isListening -> Brush.radialGradient(
                            colors = listOf(Color(0xFFEF4444), Color(0xFFDC2626))
                        )
                        else -> Brush.radialGradient(
                            colors = listOf(PCIBlue, Color(0xFF1E40AF))
                        )
                    },
                    shape = CircleShape
                )
                .clickable(enabled = isEnabled) {
                    if (isListening) {
                        onStopListening()
                    } else {
                        onStartListening()
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = when {
                    !isEnabled -> Icons.Default.MicOff
                    isListening -> Icons.Default.Stop
                    else -> Icons.Default.Mic
                },
                contentDescription = null,
                modifier = Modifier.size(screenConfig.iconSize * 1.5f),
                tint = Color.White.copy(alpha = if (isEnabled) 1f else 0.6f)
            )
        }
    }
}

@Composable
fun VoiceStatusText(
    uiState: VoiceSearchUiState,
    screenConfig: ScreenConfig
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(screenConfig.spacing / 2)
    ) {
        Text(
            text = when {
                uiState.isListening -> "🎤 Escuchando..."
                uiState.error != null -> "❌ ${uiState.error}"
                uiState.hasSearched && uiState.searchQuery.isNotEmpty() ->
                    "Buscando: \"${uiState.searchQuery}\""
                else -> "Toca para buscar por voz"
            },
            fontSize = screenConfig.bodySize,
            color = when {
                uiState.isListening -> Color(0xFFEF4444)
                uiState.error != null -> Color(0xFFEF4444)
                uiState.hasSearched -> PCIBlue
                else -> Color(0xFF9CA3AF)
            },
            textAlign = TextAlign.Center,
            fontWeight = if (uiState.isListening) FontWeight.Medium else FontWeight.Normal
        )

        Text(
            text = when {
                uiState.isListening -> "Di el nombre del producto que buscas..."
                uiState.error != null -> "Toca el micrófono para intentar de nuevo"
                uiState.hasSearched -> "${uiState.searchResults.size} productos encontrados"
                else -> "Busca productos, marcas o categorías"
            },
            fontSize = screenConfig.captionSize,
            color = Color(0xFF6B7280),
            textAlign = TextAlign.Center,
            lineHeight = (screenConfig.captionSize.value * 1.3f).sp
        )
    }
}

@Composable
fun VoiceSearchResults(
    searchResults: List<Product>,
    searchQuery: String,
    screenConfig: ScreenConfig
) {
    if (searchResults.isEmpty()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(screenConfig.spacing)
        ) {
            Icon(
                imageVector = Icons.Default.SearchOff,
                contentDescription = null,
                modifier = Modifier.size(screenConfig.iconSize * 2f),
                tint = Color.Gray
            )

            Text(
                text = "Sin resultados",
                fontSize = screenConfig.bodySize,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "No se encontraron productos para \"$searchQuery\"",
                fontSize = screenConfig.captionSize,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(screenConfig.spacing / 2),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(searchResults.take(5)) { product ->
                VoiceSearchProductCard(
                    product = product,
                    screenConfig = screenConfig
                )
            }

            if (searchResults.size > 5) {
                item {
                    Text(
                        text = "+ ${searchResults.size - 5} productos más",
                        fontSize = screenConfig.captionSize,
                        color = PCIBlue,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(screenConfig.spacing)
                    )
                }
            }
        }
    }
}

@Composable
fun VoiceSearchProductCard(
    product: Product,
    screenConfig: ScreenConfig
) {
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
                val imageUrl = product.image_url.firstOrNull()

                if (!imageUrl.isNullOrEmpty()) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = product.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
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
                Text(
                    text = product.name.trim().take(35) + if (product.name.length > 35) "..." else "",
                    fontSize = screenConfig.captionSize,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(screenConfig.spacing / 3))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "$${product.price}",
                        fontSize = screenConfig.bodySize,
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

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                modifier = Modifier.size(screenConfig.iconSize * 0.8f),
                tint = Color(0xFF9CA3AF)
            )
        }
    }
}