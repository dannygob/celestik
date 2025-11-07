package com.example.celestik.ui.screen

import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.celestik.manager.AprilTagManager
import com.example.celestik.processing.FrameAnalyzer
import com.example.celestik.viewmodel.CameraViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun CameraScreen(
    navController: NavController,
    aprilTagManager: AprilTagManager,
    viewModel: CameraViewModel = viewModel()
) {
    val context = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val previewView = remember { PreviewView(context) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val cameraProvider = cameraProviderFuture.get()
        val preview = androidx.camera.core.Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        val imageAnalyzer = ImageAnalysis.Builder()
            .build()
            .also {
                it.setAnalyzer(Dispatchers.Default.asExecutor()) { imageProxy ->
                    coroutineScope.launch {
                        try {
                            val result = FrameAnalyzer(viewModel.sharedViewModel).analyze(imageProxy)
                            viewModel.updateResult(result)
                        } catch (e: Exception) {
                            Log.e("CameraScreen", "Error analyzing frame", e)
                            viewModel.setError(e.message ?: "Error desconocido")
                        } finally {
                            imageProxy.close()
                        }
                    }
                }
            }

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                context as androidx.lifecycle.LifecycleOwner,
                cameraSelector,
                preview,
                imageAnalyzer
            )
        } catch (e: Exception) {
            Log.e("CameraScreen", "Error al iniciar la cámara", e)
            viewModel.setError("No se pudo iniciar la cámara")
        }
    }

    val result by viewModel.result.collectAsState()
    val error by viewModel.error.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = { previewView }, modifier = Modifier.weight(1f))

        if (error != null) {
            Text("Error: $error", color = MaterialTheme.colorScheme.error)
        } else {
            result?.let {
                Text("Resultado: ${it.label}", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}
