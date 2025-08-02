package com.example.celestik.manager

import com.taylor.demo.apriltag.ApriltagNative
import org.opencv.core.Mat

class AprilTagManager {

    data class Marker(
        val id: Int,
        val hamming: Int,
        val decisionMargin: Float,
        val center: DoubleArray,
        val corners: DoubleArray,
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Marker

            if (id != other.id) return false
            if (hamming != other.hamming) return false
            if (decisionMargin != other.decisionMargin) return false
            if (!center.contentEquals(other.center)) return false
            if (!corners.contentEquals(other.corners)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = id
            result = 31 * result + hamming
            result = 31 * result + decisionMargin.hashCode()
            result = 31 * result + center.contentHashCode()
            result = 31 * result + corners.contentHashCode()
            return result
        }
    }

    fun init() {
        ApriltagNative.apriltagInit("tag36h11", 1, 1.0f, 0.0f, 4, 1, 0.25)
    }

    fun detectMarkers(image: Mat): List<Marker> {
        val yuvData = ByteArray(image.total().toInt() * image.channels())
        image.get(0, 0, yuvData)
        val detections = ApriltagNative.apriltagDetectYuv(
            yuvData,
            "rear_camera",
            0,
            1,
            image.width(),
            image.height()
        )
        val markers = mutableListOf<Marker>()
        if (detections != null) {
            for (detection in detections) {
                markers.add(
                    Marker(
                        detection.id,
                        detection.hamming,
                        detection.decisionMargin,
                        detection.center,
                        detection.corners
                    )
                )
            }
        }
        return markers
    }
}