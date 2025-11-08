package com.example.celestik.ui.screen

import android.util.Log
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.celestik.manager.AprilTagManager
import com.example.celestik.opencv.FrameAnalyzer
import com.example.celestik.utils.matToImageBitmap
import com.example.celestik.viewmodel.CameraViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

/**
 * Displays a live camera feed and analyzes frames using FrameAnalyzer.
 * Shows result or error messages based on analysis outcome.
 *
 * @param navController Navigation controller for screen transitions.
 * @param aprilTagManager (Unused) AprilTag manager instance.
 * @param viewModel Camera view model containing shared state.
 */
@Composable
fun CameraScreen(
    navController: NavController,
    aprilTagManager: AprilTagManager, // TODO: Remove if unused
    viewModel: CameraViewModel = viewModel()
) {
    val context = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val previewView = remember { PreviewView(context) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val cameraProvider = cameraProviderFuture.get()
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        val imageAnalyzer = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also {
                it.setAnalyzer(Dispatchers.Default.asExecutor()) { imageProxy ->
                    coroutineScope.launch {
                        try {
                            val mat = imageProxyToMat(imageProxy)
                            val result = FrameAnalyzer(viewModel.sharedViewModel).analyze(mat)
                            viewModel.updateResult(result)
                        } catch (e: Exception) {
                            Log.e("CameraScreen", "Error analyzing frame", e)
                            viewModel.setError(e.message ?: "Unknown error")
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
            Log.e("CameraScreen", "Camera initialization failed", e)
            viewModel.setError("Unable to start camera")
        }
    }

    val result by viewModel.result.collectAsState()
    val error by viewModel.error.collectAsState()

    val imageBitmap: ImageBitmap? = result?.annotatedMat?.let { matToImageBitmap(it) }

    Column(modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = { previewView }, modifier = Modifier.weight(1f))

        imageBitmap?.let {
            Image(bitmap = it, contentDescription = "Processed frame", modifier = Modifier.fillMaxWidth())
        }

        if (error != null) {
            Text("Error: $error", color = MaterialTheme.colorScheme.error)
        } else {
            result?.let {
                Text("Contours: ${it.contours.size}, Markers: ${it.markers.size}", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

/**
 * Converts ImageProxy to OpenCV Mat in BGR format.
 */
private fun imageProxyToMat(image: ImageProxy): Mat {
    val yBuffer = image.planes[0].buffer
    val ySize = yBuffer.remaining()
    val yData = ByteArray(ySize)
    yBuffer.get(yData)

    val matY = Mat(image.height, image.width, CvType.CV_8UC1)
    matY.put(0, 0, yData)

    val matBGR = Mat()
    Imgproc.cvtColor(matY, matBGR, Imgproc.COLOR_GRAY2BGR)
    return matBGR
}