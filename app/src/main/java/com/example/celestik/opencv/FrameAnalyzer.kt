package com.example.celestik.opencv


import android.util.Log
import com.example.celestik.manager.AprilTagManager
import com.example.celestik.manager.ArUcoManager
import com.example.celestik.viewmodel.SharedViewModel
import com.google.android.libraries.mapsplatform.transportation.consumer.model.MarkerType
import org.opencv.aruco.Aruco
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import org.opencv.imgproc.Imgproc.undistort
import org.opencv.video.Video


class FrameAnalyzer(private val sharedViewModel: SharedViewModel) {

    data class Marker(val id: Int, val corners: Mat)

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

    fun analyze(mat: Mat): AnalysisResult {
        val grayMat = Mat()
        val edges = Mat()
        val thresholdedImage = Mat()
        val watershedMarkers = Mat()
        val holes = Mat()

        try {
            Imgproc.cvtColor(mat, grayMat, Imgproc.COLOR_BGR2GRAY)
            Imgproc.GaussianBlur(grayMat, grayMat, Size(5.0, 5.0), 0.0)

            Imgproc.adaptiveThreshold(
                grayMat,
                thresholdedImage,
                255.0,
                Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,
                Imgproc.THRESH_BINARY,
                11,
                2.0
            )

            val markersMat = generateWatershedMarkers(thresholdedImage)
            Imgproc.watershed(mat, markersMat)
            watershedMarkers.setTo(Scalar(255.0), markersMat)

            val contours = findContours(watershedMarkers)
            val filteredContours = filterContours(contours, 100.0)
            val deformations = detectDeformations(filteredContours)
            val detectedHoles = detectHoles(grayMat)
            detectedHoles.copyTo(holes)

            prevGrayMat?.let {
                detectDeformationsWithOpticalFlow(it, grayMat)
            }
            prevGrayMat = grayMat.clone()

            val markers = when (sharedViewModel.markerType.value) {
                MarkerType.ARUCO -> arucoManager.detectMarkers(mat).map { Marker(it.id, it.corners) }
                MarkerType.APRILTAG -> aprilTagManager.detectMarkers(mat).map { Marker(it.id, cornersToMat(it.corners)) }

            }

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
            thresholdedImage.release()
            watershedMarkers.release()
            holes.release()
        }
    }

    private fun generateWatershedMarkers(binary: Mat): Mat {
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
        Imgproc.findContours(image, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE)
        hierarchy.release()
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

    fun detectCountersinks(image: Mat, template: Mat): Point? {
        val result = Mat()
        Imgproc.matchTemplate(image, template, result, Imgproc.TM_CCOEFF_NORMED)
        val mmr = Core.minMaxLoc(result)
        return if (mmr.maxVal > 0.8) mmr.maxLoc else null
    }

    fun detectDeformationsWithOpticalFlow(prevFrame: Mat, nextFrame: Mat): List<Point> {
        val prevGray = Mat()
        val nextGray = Mat()
        Imgproc.cvtColor(prevFrame, prevGray, Imgproc.COLOR_BGR2GRAY)
        Imgproc.cvtColor(nextFrame, nextGray, Imgproc.COLOR_BGR2GRAY)

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
}
