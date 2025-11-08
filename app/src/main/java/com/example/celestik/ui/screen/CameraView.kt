import android.content.pm.PackageManager
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.celestik.opencv.OpenCVInitializer
import com.example.celestik.viewmodel.MainViewModel
import java.util.concurrent.Executors
import java.util.jar.Manifest

/**
 * Displays a camera preview and performs real-time image classification.
 * Initializes OpenCV and handles camera permission checks.
 *
 * @param viewModel ViewModel for storing classification results.
 * @param modifier Optional layout modifier.
 */
@Composable
fun CameraView(
    viewModel: MainViewModel = viewModel(),
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    var permissionGranted by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        permissionGranted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (!permissionGranted) {
            Log.e("CameraView", "Camera permission not granted.")
        }

        val success = OpenCVInitializer.initOpenCV(context)
        if (!success) Log.e("CameraView", "Failed to initialize OpenCV")
    }

    DisposableEffect(Unit) {
        onDispose { cameraExecutor.shutdown() }
    }

    if (!permissionGranted) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Camera permission not granted.")
        }
        return
    }

    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                PreviewView(ctx).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }.also { previewView ->
                    startCamera(ctx, previewView, cameraExecutor, viewModel)
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}
