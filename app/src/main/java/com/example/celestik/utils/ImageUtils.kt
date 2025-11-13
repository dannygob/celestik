package com.example.celestik.utils

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.createBitmap
import org.opencv.android.Utils
import org.opencv.core.Mat

fun matToImageBitmap(mat: Mat): ImageBitmap {
    if (mat.empty() || mat.cols() <= 0 || mat.rows() <= 0) {
        throw IllegalArgumentException("El Mat proporcionado está vacío o tiene dimensiones inválidas.")
    }
    val bitmap = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888)
    Utils.matToBitmap(mat, bitmap)
    return bitmap.asImageBitmap()
}
