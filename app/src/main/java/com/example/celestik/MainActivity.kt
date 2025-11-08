package com.example.celestik

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController


import com.example.celestik.opencv.OpenCVInitializer // 👈 Asegúrate de importar esto

/**
 * MainActivity is the entry point of the Celestik app.
 * It sets up the Compose UI, applies the theme, and initializes navigation.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Inicializar OpenCV
        val openCvReady = OpenCVInitializer.init()
        Log.d("MainActivity", "OpenCV ready: $openCvReady")

        enableEdgeToEdge()
        setContent {
            CelesticTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
                    NavigationGraph(navController = navController, modifier = Modifier.padding(paddingValues))
                }
            }
        }
    }
}