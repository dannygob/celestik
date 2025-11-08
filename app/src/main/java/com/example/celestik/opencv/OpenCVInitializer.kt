package com.example.celestik.opencv

import android.util.Log
import org.opencv.android.OpenCVLoader

object OpenCVInitializer {
    fun init(): Boolean {
        val success = OpenCVLoader.initDebug()
        if (success) {
            Log.d("OpenCV", "OpenCV initialized successfully")
        } else {
            Log.e("OpenCV", "OpenCV initialization failed")
        }
        return success
    }
}