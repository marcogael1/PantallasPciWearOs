package com.example.plantillasproyecto.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.MediaRecorder
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer

object VoiceUtils {
    fun checkVoiceRecognitionAvailability(context: Context): Boolean {
        return try {
            val packageManager = context.packageManager
            val activities = packageManager.queryIntentActivities(
                Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH),
                0
            )
            val isRecognitionAvailable = SpeechRecognizer.isRecognitionAvailable(context)

            println("🎤 Actividades de reconocimiento: ${activities.size}")
            println("🎤 SpeechRecognizer disponible: $isRecognitionAvailable")

            activities.isNotEmpty() && isRecognitionAvailable
        } catch (e: Exception) {
            println("❌ Error verificando reconocimiento: ${e.message}")
            false
        }
    }

    fun checkMicrophoneAvailability(context: Context): Boolean {
        return try {
            val hasBuiltInMic = context.packageManager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE)
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            val isMicMuted = audioManager.isMicrophoneMute

            println("🎤 Micrófono integrado: $hasBuiltInMic")
            println("🎤 Micrófono silenciado: $isMicMuted")

            hasBuiltInMic && !isMicMuted
        } catch (e: Exception) {
            println("❌ Error verificando micrófono: ${e.message}")
            false
        }
    }

    fun testMicrophoneRecording(context: Context): Boolean {
        return try {
            val mediaRecorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                setOutputFile("/dev/null")
            }

            mediaRecorder.prepare()
            mediaRecorder.start()
            Thread.sleep(500)
            mediaRecorder.stop()
            mediaRecorder.release()

            println("✅ Test de grabación exitoso")
            true
        } catch (e: Exception) {
            println("❌ Error en test de grabación: ${e.message}")
            false
        }
    }
}