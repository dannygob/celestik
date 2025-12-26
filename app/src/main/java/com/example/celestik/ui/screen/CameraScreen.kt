package com.example.celestik.ui.screen

// IMPORTANTE: Estos dos son vitales para que el "by" funcione
import android.util.Log
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.celestik.manager.AprilTagManager
import com.example.celestik.opencv.FrameAnalyzer
import com.example.celestik.viewmodel.CameraViewModel
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc
import java.util.concurrent.Executors

/**
 * CameraScreen: Interfaz principal corregida.
 * Une CameraX con el motor de OpenCV sin conflictos de nombres.
 */
@Composable
fun CameraScreen(
    navController: NavController,
    aprilTagManager: AprilTagManager,
    viewModel: CameraViewModel = viewModel(),
) {
    val context = LocalContext.current
    val lifecycleOwner = context as LifecycleOwner
    val previewView = remember { PreviewView(context) }

    // Reutilizamos el motor de análisis para evitar fugas de memoria
    val analyzerEngine = remember { FrameAnalyzer(viewModel.sharedViewModel) }
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }

    // Recolectamos el estado del análisis desde el ViewModel
    val result by viewModel.result.collectAsState(initial = null)
    val error by viewModel.error.collectAsState(initial = null)

    LaunchedEffect(Unit) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.surfaceProvider = previewView.surfaceProvider
            }

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_YUV_420_888)
                .build()
                .also { analysis ->
                    analysis.setAnalyzer(cameraExecutor) { imageProxy ->
                        try {
                            val mat = imageProxyToMat(imageProxy)
                            if (mat != null) {
                                // Ejecuta el análisis industrial
                                val analysisResult = analyzerEngine.analyze(mat)
                                // Actualiza la UI a través del ViewModel
                                viewModel.updateResult(analysisResult)
                                mat.release()
                            }
                        } catch (e: Exception) {
                            Log.e("CameraScreen", "Error en análisis", e)
                            viewModel.setError(e.message ?: "Fallo en el motor de visión")
                        } finally {
                            imageProxy.close()
                        }
                    }
                }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    imageAnalysis
                )
            } catch (e: Exception) {
                Log.e("CameraScreen", "Error de binding", e)
            }
        }, androidx.core.content.ContextCompat.getMainExecutor(context))
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Capa inferior: Vista previa de la cámara
        AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())

        // Capa superior: Resultados y Errores (Overlay)
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            error?.let { msg ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Text(
                        text = "Error: $msg",
                        modifier = Modifier.padding(8.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            result?.let { res ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f)
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Detección Industrial",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Contornos: ${res.contours.size}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Marcadores ArUco: ${res.markers.size}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }

    // Limpieza al cerrar la pantalla
    DisposableEffect(Unit) {
        onDispose {
            cameraExecutor.shutdown()
        }
    }
}

/**
 * Conversión de alto rendimiento de YUV a BGR (OpenCV)
 */
private fun imageProxyToMat(image: ImageProxy): Mat? {
    return try {
        val yBuffer = image.planes[0].buffer
        val uBuffer = image.planes[1].buffer
        val vBuffer = image.planes[2].buffer

        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()

        val nv21 = ByteArray(ySize + uSize + vSize)

        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)

        val yuvMat = Mat(image.height + image.height / 2, image.width, CvType.CV_8UC1)
        yuvMat.put(0, 0, nv21)

        val bgrMat = Mat()
        Imgproc.cvtColor(yuvMat, bgrMat, Imgproc.COLOR_YUV2BGR_NV21)

        yuvMat.release()
        bgrMat
    } catch (e: Exception) {
        null
    }
}