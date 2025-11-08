package com.example.celestik.manager

import org.opencv.aruco.Aruco
import org.opencv.core.Mat
import org.opencv.core.MatOfInt

/**
 * ArUcoManager handles detection of ArUco markers using OpenCV.
 * It wraps the native detection logic and returns structured marker data.
 */
class ArUcoManager {

    /**
     * Represents a detected ArUco marker with its ID and corner positions.
     */
    data class Marker(
        val id: Int,     // Marker ID
        val corners: Mat // 4x1 matrix of corner points
    )

    /**
     * Detects ArUco markers in the given image and returns a list of Marker objects.
     */
    fun detectMarkers(image: Mat): List<Marker> {
        val dictionary = Aruco.getPredefinedDictionary(Aruco.DICT_6X6_250)
        val corners = ArrayList<Mat>()
        val ids = MatOfInt()

        // Perform marker detection
        Aruco.detectMarkers(image, dictionary, corners, ids)

        val markers = mutableListOf<Marker>()
        if (ids.total() > 0) {
            val idsArray = IntArray(ids.total().toInt())
            ids.get(0, 0, idsArray)

            // Ensure safe indexing in case of mismatch
            val count = minOf(idsArray.size, corners.size)
            for (i in 0 until count) {
                markers.add(Marker(idsArray[i], corners[i]))
            }
        }

        return markers
    }
}
