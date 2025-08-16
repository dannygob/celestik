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
    )

    // Detector configurado con la familia tag36h11
    private val detector: AprilTagDetector = AprilTagDetector.Builder()
        .addFamily("tag36h11")
        .build()

    // Simula init(), no es estrictamente necesario pero para mantener el patrón
    fun init() {
        // Aquí podrías hacer inicializaciones si fuera necesario
        // La librería oficial no requiere init explícito aparte del Builder
    }

    fun detectMarkers(image: Mat): List<Marker> {
        // Convertir imagen a escala de grises si es necesario
        val gray = Mat()
        if (image.channels() > 1) {
            Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY)
        } else {
            image.copyTo(gray)
        }

        // Extraer bytes de la imagen gris
        val width = gray.width()
        val height = gray.height()
        val grayBytes = ByteArray(width * height)
        gray.get(0, 0, grayBytes)

        // Detectar los AprilTags usando la librería oficial
        val detections: List<AprilTagDetection> = detector.detect(grayBytes, width, height)

        // Mapear detecciones a Marker para mantener tu estructura
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
        detector.close()  // Liberar recursos cuando termines
    }
}
