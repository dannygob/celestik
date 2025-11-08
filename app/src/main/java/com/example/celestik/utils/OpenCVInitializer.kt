package com.example.celestik.utils

import android.content.Context
import android.util.Log
import org.opencv.android.OpenCVLoader

/**
 * Initializes OpenCV using debug mode and logs the result.
 * Should be called once during app startup or camera activation.
 */
object OpenCVInitializer {

    fun initOpenCV(context: Context) {
        if (OpenCVLoader.initDebug()) {
            Log.d("OpenCV", "OpenCV initialized successfully") // TODO: Localize
        } else {
            Log.e("OpenCV", "OpenCV initialization failed") // TODO: Localize
        }
    }
}
