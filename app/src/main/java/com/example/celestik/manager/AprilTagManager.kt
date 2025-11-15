package com.example.celestik.manager


import edu.wpi.first.apriltag.AprilTagDetection
import edu.wpi.first.apriltag.AprilTagDetector
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc


/**
 * AprilTagManager gestiona la detección de etiquetas AprilTag físicas
 * y la generación de etiquetas virtuales para elementos detectados.
 */
class AprilTagManager {

    /**
     * Representa una etiqueta AprilTag (física o virtual) con metadatos y geometría.
     */
    data class Marker(
        val id: Int,                      // ID único del tag
        val hamming: Int,                // Nivel de corrección de errores
        val decisionMargin: Float,       // Confianza de detección
        val center: DoubleArray,         // Coordenadas [x, y] del centro
        val corners: DoubleArray         // Coordenadas de las esquinas (plano 2D)
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

    // Detector nativo para etiquetas físicas (tag36h11)
    private val detector: AprilTagDetector = AprilTagDetector.Builder
        .addFamily("tag36h11")
        .build()

    /**
     * Inicialización opcional para configuración futura.
     */
    fun init() {
        // Placeholder para logs, configuración dinámica, etc.
    }

    /**
     * Detecta etiquetas físicas AprilTag en una imagen.
     */
    fun detectMarkers(image: Mat): List<Marker> {
        val gray = Mat()
        if (image.channels() > 1) {
            Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY)
        } else {
            image.copyTo(gray)
        }

        val detections = detector.detect(gray)
            .filterIsInstance<AprilTagDetection>()

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
     * Genera una etiqueta virtual para un elemento detectado.
     * @param featureId ID único del elemento
     * @param position Coordenadas (x, y) del centro del elemento
     * @param size Tamaño visual del tag (por defecto 20 px)
     */
    fun generateVirtualTagForFeature(
        featureId: Int,
        position: Pair<Double, Double>,
        size: Double = 20.0
    ): Marker {
        val (x, y) = position
        val half = size / 2.0

        return Marker(
            id = featureId,
            hamming = 0,
            decisionMargin = 1.0f,
            center = doubleArrayOf(x, y),
            corners = doubleArrayOf(
                x - half, y - half,
                x + half, y - half,
                x + half, y + half,
                x - half, y + half
            )
        )
    }

    /**
     * Libera los recursos nativos del detector.
     */
    fun close() {
        detector.close()
    }
}
