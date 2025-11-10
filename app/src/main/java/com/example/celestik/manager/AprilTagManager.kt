package com.example.celestik.manager

import edu.wpi.first.apriltag.AprilTagDetection
import edu.wpi.first.apriltag.AprilTagDetector
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc


/**
 * AprilTagManager handles detection of AprilTags using WPILib and OpenCV.
 * It wraps the native detector and exposes structured results.
 */
class AprilTagManager {

    /**
     * Represents a detected AprilTag marker with metadata and geometry.
     */
    data class Marker(
        val id: Int,                      // Unique tag ID
        val hamming: Int,                // Hamming error correction level
        val decisionMargin: Float,       // Confidence score
        val center: DoubleArray,         // [x, y] center coordinates
        val corners: DoubleArray         // Flattened array of corner points
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Marker

            return id == other.id &&
                hamming == other.hamming &&
                decisionMargin == other.decisionMargin &&
                center.contentEquals(other.center) &&
                corners.contentEquals(other.corners)
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

    /**
     * Optional initialization hook for future setup logic.
     */
    fun init() {
        // Placeholder for future initialization logic (e.g., logging, configuration)
    }

    /**
     * Detects AprilTags in the given image and returns a list of structured markers.
     */
    fun detectMarkers(image: Mat): List<Marker> {
        // Convert image to grayscale if necessary
        val gray = Mat()
        if (image.channels() > 1) {
            Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY)
        } else {
            image.copyTo(gray)
        }

        // Detect AprilTags using the native detector
        val detections = detector.detect(gray)
            .filterIsInstance<AprilTagDetection>()

        // Map detections to Marker objects
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

    /**
     * Releases native resources held by the detector.
     * Should be called when the manager is no longer needed.
     */
    fun close() {
        detector.close()
    }
}
