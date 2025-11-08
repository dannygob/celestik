package com.example.celestik.opencv

import android.util.Log
import androidx.compose.ui.geometry.Size
import com.example.celestik.manager.AprilTagManager
import com.example.celestik.manager.ArUcoManager
import com.example.celestik.viewmodel.MarkerType
import com.example.celestik.viewmodel.SharedViewModel
import org.opencv.aruco.Aruco
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import org.opencv.video.Video

/**
 * Performs image analysis using OpenCV, including marker detection,
 * contour extraction, deformation analysis, and optical flow tracking.
 */
class FrameAnalyzer(private val sharedViewModel: SharedViewModel) {

    /**
     * Represents a detected marker with its ID and corner matrix.
     */
    data class Marker(val id: Int, val corners: Mat)

    /**
     * Encapsulates the result of a frame analysis.
     */
    data class AnalysisResult(
        val contours: List<MatOfPoint>,
        val annotatedMat: Mat,
        val markers: List<Marker>,
    )

    private var prevGrayMat: Mat? = null
    private val arucoManager = ArUcoManager()
    private val aprilTagManager = AprilTagManager()

    init {
        aprilTagManager.init()
    }

    private fun cornersToMat(corners: DoubleArray): Mat {
        val mat = Mat(4, 1, CvType.CV_32FC2)
        mat.put(0, 0, *corners)
        return mat
    }

    /**
     * Main analysis function that processes a frame and returns results.
     */
    fun analyze(mat: Mat): AnalysisResult {
        val grayMat = Mat()
        val edges = Mat()

        try {
            // Preprocessing
            Imgproc.cvtColor(mat, grayMat, Imgproc.COLOR_BGR2GRAY)
            Imgproc.GaussianBlur(grayMat, grayMat, Size(5.0, 5.0), 0.0)

            val thresholdedImage = applyAdaptiveThresholding(grayMat)
            val watershedMarkers = applyWatershed(thresholdedImage)
            val contours = findContours(watershedMarkers)
            val filteredContours = filterContours(contours, 100.0)
            val deformations = detectDeformations(filteredContours)
            val holes = detectHoles(grayMat)

            // Optical flow deformation tracking
            prevGrayMat?.let { detectDeformationsWithOpticalFlow(it, grayMat) }
            prevGrayMat = grayMat.clone()

            // Marker detection
            val markers = when (sharedViewModel.markerType.value) {
                MarkerType.ARUCO -> arucoManager.detectMarkers(mat).map { Marker(it.id, it.corners) }
                MarkerType.APRILTAG -> aprilTagManager.detectMarkers(mat).map { Marker(it.id, cornersToMat(it.corners)) }
            }

            // Annotate result
            val annotatedMat = mat.clone()
            Imgproc.drawContours(annotatedMat, filteredContours, -1, Scalar(0.0, 255.0, 0.0), 2)
            Imgproc.drawContours(annotatedMat, deformations, -1, Scalar(255.0, 0.0, 0.0), 2)
            for (i in 0 until holes.cols()) {
                val circle = holes.get(0, i)
                val center = Point(circle[0], circle[1])
                val radius = circle[2].toInt()
                Imgproc.circle(annotatedMat, center, radius, Scalar(0.0, 0.0, 255.0), 2)
            }

            if (markers.isNotEmpty()) {
                Aruco.drawDetectedMarkers(annotatedMat, markers.map { it.corners }, Mat())
            }

            return AnalysisResult(contours, annotatedMat, markers)

        } catch (e: Exception) {
            Log.e("FrameAnalyzer", "Error analyzing frame", e)
            return AnalysisResult(emptyList(), mat, emptyList())
        } finally {
            grayMat.release()
            edges.release()
        }
    }

    fun detectEdges(image: Mat): Mat {
        val edges = Mat()
        Imgproc.Canny(image, edges, 100.0, 200.0)
        return edges
    }

    fun applyCalibration(image: Mat, cameraMatrix: Mat, distortionCoeffs: Mat): Mat {
        val undistortedImage = Mat()
        Imgproc.undistort(image, undistortedImage, cameraMatrix, distortionCoeffs)
        return undistortedImage
    }

    fun extractDimensionsFromContours(contours: List<MatOfPoint>): List<Double> {
        val dimensions = mutableListOf<Double>()
        for (contour in contours) {
            val rect = Imgproc.boundingRect(contour)
            dimensions.add(rect.width.toDouble())
            dimensions.add(rect.height.toDouble())
        }
        return dimensions
    }

    private fun findContours(image: Mat): List<MatOfPoint> {
        val contours = ArrayList<MatOfPoint>()
        val hierarchy = Mat()
        Imgproc.findContours(
            image,
            contours,
            hierarchy,
            Imgproc.RETR_EXTERNAL,
            Imgproc.CHAIN_APPROX_SIMPLE
        )
        return contours
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

    fun detectDeformations(contours: List<MatOfPoint>): List<MatOfPoint> {
        val deformations = mutableListOf<MatOfPoint>()
        for (contour in contours) {
            val approx = MatOfPoint2f()
            val contour2f = MatOfPoint2f(*contour.toArray())
            Imgproc.approxPolyDP(contour2f, approx, 0.04 * Imgproc.arcLength(contour2f, true), true)
            if (approx.toArray().size > 4) {
                deformations.add(MatOfPoint(*approx.toArray()))
            }
        }
        return deformations
    }

    fun applyAdaptiveThresholding(image: Mat): Mat {
        val thresholdedImage = Mat()
        Imgproc.adaptiveThreshold(
            image,
            thresholdedImage,
            255.0,
            Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,
            Imgproc.THRESH_BINARY,
            11,
            2.0
        )
        return thresholdedImage
    }

    fun filterContours(contours: List<MatOfPoint>, minArea: Double): List<MatOfPoint> {
        val filteredContours = mutableListOf<MatOfPoint>()
        for (contour in contours) {
            val area = Imgproc.contourArea(contour)
            if (area > minArea) {
                filteredContours.add(contour)
            }
        }
        return filteredContours
    }

    fun applyWatershed(image: Mat): Mat {
        val markers = Mat()
        Imgproc.connectedComponents(image, markers)
        Imgproc.watershed(image, markers)
        return markers
    }

    fun detectCountersinks(image: Mat, template: Mat): Mat {
        val result = Mat()
        Imgproc.matchTemplate(image, template, result, Imgproc.TM_CCOEFF_NORMED)
        return result
    }

    fun detectDeformationsWithOpticalFlow(prevFrame: Mat, nextFrame: Mat): MatOfPoint2f {
        val prevPts = MatOfPoint2f()
        Imgproc.goodFeaturesToTrack(prevFrame, prevPts, 100, 0.3, 7.0)
        val nextPts = MatOfPoint2f()
        val status = MatOfByte()
        val err = MatOfFloat()
        Video.calcOpticalFlowPyrLK(prevFrame, nextFrame, prevPts, nextPts, status, err)
        return nextPts
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
}
