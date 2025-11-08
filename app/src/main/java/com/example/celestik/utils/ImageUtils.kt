package com.example.celestik.utils

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import org.opencv.android.Utils
import org.opencv.core.Mat
import androidx.core.graphics.createBitmap

fun matToImageBitmap(mat: Mat): ImageBitmap {
    val bitmap = createBitmap(mat.cols(), mat.rows())
    Utils.matToBitmap(mat, bitmap)
    return bitmap.asImageBitmap()
}