package com.example.celestik.opencv

import android.util.Log
import org.opencv.android.OpenCVLoader

/**
 * OpenCVInitializer handles native OpenCV initialization and native JNI library loading.
 * Call init() once at app startup (e.g., in MainActivity.onCreate).
 */
object OpenCVInitializer {

    fun init(): Boolean {
        val success = OpenCVLoader.initDebug()
        if (success) {
            Log.d("OpenCV", "OpenCV initialized successfully")
            try {
                System.loadLibrary("native_opencv")
                Log.d("OpenCV", "Native JNI library loaded")
            } catch (e: UnsatisfiedLinkError) {
                Log.e("OpenCV", "Failed to load native_opencv: ${e.message}")
                return false
            }
        } else {
            Log.e("OpenCV", "OpenCV initialization failed")
        }
        return success
    }
}