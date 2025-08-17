package com.example.celestik.manager

import edu.wpi.first.apriltag.AprilTagDetection
import edu.wpi.first.apriltag.AprilTagDetector
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

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

    // Detector configured with the tag36h11 family
    private val detector: AprilTagDetector = AprilTagDetector.Builder()
        .addFamily("tag36h11")
        .build()

    // Simulates init(), not strictly necessary but to maintain the pattern
    fun init() {
        // Here you could do initializations if necessary
        // The official library does not require explicit init apart from the Builder
    }

    fun detectMarkers(image: Mat): List<Marker> {
        // Convert image to grayscale if necessary
        val gray = Mat()
        if (image.channels() > 1) {
            Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY)
        } else {
            image.copyTo(gray)
        }

        // Extract bytes from the gray image
        val width = gray.width()
        val height = gray.height()
        val grayBytes = ByteArray(width * height)
        gray.get(0, 0, grayBytes)

        // Detect the AprilTags using the official library
        val detections: List<AprilTagDetection> =
            detector.detect(grayBytes, width, height) as List<AprilTagDetection>

        // Map detections to Marker to maintain your structure
        return detections.map { detection ->
            Marker(
                id = detection.id,
                hamming = detection.hamming,
                decisionMargin = detection.decisionMargin,
                center = doubleArrayOf(detection.center[0], detection.center[1]),
                corners = detection.corners.flatMap { it.toList() }.toDoubleArray()
            )
        }
    }

    fun close() {
        detector.close()  // Release resources when finished
    }
}
