package com.celestik.ui.screens

import android.Manifest
import android.content.Context
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.celestik.viewmodel.CameraViewModel
import com.celestik.processing.FrameAnalyzer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun CameraScreen(
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
                it.setAnalyzer(Dispatchers.Default.asExecutor(), { imageProxy ->
                    coroutineScope.launch {
                        try {
                            val result = FrameAnalyzer.analyze(imageProxy)
                            viewModel.updateResult(result)
                        } catch (e: Exception) {
                            Log.e("CameraScreen", "Error analyzing frame", e)
                            viewModel.setError(e.message ?: "Error desconocido")
                        } finally {
                            imageProxy.close()
                        }
                    }
                })
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
            Text("Error: $error", color = MaterialTheme.colors.error)
        } else {
            result?.let {
                Text("Resultado: ${it.label}", style = MaterialTheme.typography.h6)
            }
        }
    }
}
