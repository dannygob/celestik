package com.example.celestik.utils

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import org.opencv.video.Video

fun matToImageBitmap(mat: Mat): ImageBitmap {
    if (mat.empty() || mat.cols() <= 0 || mat.rows() <= 0) {
        throw IllegalArgumentException("El Mat proporcionado está vacío o tiene dimensiones inválidas.")
    }
    val bitmap = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888)
    Utils.matToBitmap(mat, bitmap)
    return bitmap.asImageBitmap()
}

fun detectEdgesCanny(image: Mat): Mat {
    val edges = Mat()
    Imgproc.Canny(image, edges, 100.0, 200.0)
    return edges
}

fun detectEdgesSobel(image: Mat): Mat {
    val gradX = Mat()
    val gradY = Mat()
    val absGradX = Mat()
    val absGradY = Mat()
    val result = Mat()

    Imgproc.Sobel(image, gradX, CvType.CV_16S, 1, 0)
    Imgproc.Sobel(image, gradY, CvType.CV_16S, 0, 1)
    Core.convertScaleAbs(gradX, absGradX)
    Core.convertScaleAbs(gradY, absGradY)
    Core.addWeighted(absGradX, 0.5, absGradY, 0.5, 0.0, result)

    return result
}

fun applyAdaptiveThreshold(image: Mat): Mat {
    val thresholded = Mat()
    Imgproc.adaptiveThreshold(
        image,
        thresholded,
        255.0,
        Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,
        Imgproc.THRESH_BINARY,
        11,
        2.0
    )
    return thresholded
}

fun generateWatershedMarkers(binary: Mat): Mat {
    val kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, Size(3.0, 3.0))
    val sureBg = Mat()
    Imgproc.dilate(binary, sureBg, kernel)

    val distTransform = Mat()
    Imgproc.distanceTransform(binary, distTransform, Imgproc.DIST_L2, 5)

    val sureFg = Mat()
    Imgproc.threshold(distTransform, sureFg, 0.7 * Core.minMaxLoc(distTransform).maxVal, 255.0, 0.0)

    val unknown = Mat()
    Core.subtract(sureBg, sureFg, unknown)

    val markers = Mat()
    sureFg.convertTo(markers, CvType.CV_32S)
    return markers
}

fun detectHoles(image: Mat): Mat {
    val circles = Mat()
    Imgproc.HoughCircles(
        image,
        circles,
        Imgproc.HOUGH_GRADIENT,
        1.0,
        image.rows().toDouble() / 8,
        200.0,
        100.0,
        0,
        0
    )
    return circles
}

fun matchTemplate(source: Mat, template: Mat, threshold: Double = 0.8): Point? {
    val result = Mat()
    Imgproc.matchTemplate(source, template, result, Imgproc.TM_CCOEFF_NORMED)
    val mmr = Core.minMaxLoc(result)
    return if (mmr.maxVal >= threshold) mmr.maxLoc else null
}

fun trackFeaturesWithOpticalFlow(prev: Mat, next: Mat): List<Point> {
    val prevGray = Mat()
    val nextGray = Mat()
    Imgproc.cvtColor(prev, prevGray, Imgproc.COLOR_BGR2GRAY)
    Imgproc.cvtColor(next, nextGray, Imgproc.COLOR_BGR2GRAY)

    val features = MatOfPoint()
    Imgproc.goodFeaturesToTrack(prevGray, features, 100, 0.3, 7.0)

    val prevPts = MatOfPoint2f(*features.toArray())
    val nextPts = MatOfPoint2f()
    val status = MatOfByte()
    val err = MatOfFloat()

    Video.calcOpticalFlowPyrLK(prevGray, nextGray, prevPts, nextPts, status, err)

    return nextPts.toArray().toList()
}

fun calculateMeasurements(contours: List<MatOfPoint>, scale: Double): List<Double> {
    val measurements = mutableListOf<Double>()
    for (contour in contours) {
        val rect = Imgproc.boundingRect(contour)
        measurements.add(rect.width * scale)
        measurements.add(rect.height * scale)
    }
    return measurements
}
