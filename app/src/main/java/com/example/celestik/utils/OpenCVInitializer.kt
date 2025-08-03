package com.example.celestik.opencv

import android.util.Log

import org.opencv.core.Mat
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.objdetect.Dictionary

object OpenCVUtils {

    fun detectArucoMarkers(imagePath: String): String {
        val dictionary: Dictionary = Aruco.getPredefinedDictionary(Aruco.DICT_6X6_250)
        val image: Mat = Imgcodecs.imread(imagePath)
        val corners = ArrayList<Mat>()
        val ids = Mat()

        Aruco.detectMarkers(image, dictionary, corners, ids)

        val result = ids.dump()
        Log.d("OpenCVUtils", "Detected ArUco IDs: $result")
        return result
    }

    // Puedes añadir más funciones aquí para usar Imgproc, Core, etc.
}