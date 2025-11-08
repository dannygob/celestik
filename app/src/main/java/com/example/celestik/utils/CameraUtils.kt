package com.example.celestik.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.google.common.util.concurrent.ListenableFuture
import org.opencv.android.Utils
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc
import androidx.core.graphics.createBitmap

/**
 * Converts an ImageProxy (YUV format) to a Bitmap using OpenCV.
 *
 * @param image ImageProxy from CameraX.
 * @return Converted Bitmap in RGB format.
 */
fun imageProxyToBitmap(image: ImageProxy): Bitmap {
    if (image.planes.isEmpty()) throw IllegalArgumentException("ImageProxy has no planes")

    val plane = image.planes[0]
    val buffer = plane.buffer
    val bytes = ByteArray(buffer.remaining())
    buffer.get(bytes)

    val yuvMat = Mat(image.height + image.height / 2, image.width, CvType.CV_8UC1)
    yuvMat.put(0, 0, bytes)

    val rgbMat = Mat()
    Imgproc.cvtColor(yuvMat, rgbMat, Imgproc.COLOR_YUV2RGB_NV21)

    val bmp = createBitmap(rgbMat.cols(), rgbMat.rows())
    Utils.matToBitmap(rgbMat, bmp)

    yuvMat.release()
    rgbMat.release()

    return bmp
}

/**
 * Checks if the app has camera permission.
 *
 * @param context Application context.
 * @return True if permission is granted, false otherwise.
 */
fun hasCameraPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
}

/**
 * Retrieves the CameraX ProcessCameraProvider instance.
 *
 * @param context Application context.
 * @return ListenableFuture for ProcessCameraProvider.
 */
fun getCameraProvider(context: Context): ListenableFuture<ProcessCameraProvider> {
    return ProcessCameraProvider.getInstance(context)
}
