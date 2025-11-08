package com.example.celestik.manager

import android.content.Context
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import org.opencv.aruco.Aruco
import org.opencv.aruco.CharucoBoard
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import java.io.File
import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

/**
 * CalibrationManager handles camera calibration using Charuco boards and OpenCV.
 * It loads/saves calibration data and computes scale factors and matrices.
 */
class CalibrationManager @Inject constructor(private val context: Context) {

    var cameraMatrix: Mat? = null
    var distortionCoeffs: Mat? = null
    var resolution: Pair<Int, Int>? = null
    var calibrationDate: String? = null

    private val calibrationFile = File(context.filesDir, "config/calibration.json").apply {
        parentFile?.mkdirs()
    }

    init {
        loadCalibration()
    }

    /**
     * Loads calibration data from JSON file.
     */
    private fun loadCalibration(): Boolean {
        return try {
            val json = JSONObject(FileInputStream(calibrationFile).bufferedReader().use { it.readText() })

            val matrixArray = json.getJSONArray("cameraMatrix")
            val matrix = Mat(3, 3, CvType.CV_64F)
            for (i in 0 until 3) {
                val row = matrixArray.getJSONArray(i)
                for (j in 0 until 3) {
                    matrix.put(i, j, row.getDouble(j))
                }
            }

            val coeffsArray = json.getJSONArray("distortionCoeffs")
            val coeffs = Mat(1, coeffsArray.length(), CvType.CV_64F)
            for (i in 0 until coeffsArray.length()) {
                coeffs.put(0, i, coeffsArray.getDouble(i))
            }

            val resArray = json.getJSONArray("resolution")
            resolution = Pair(resArray.getInt(0), resArray.getInt(1))
            calibrationDate = json.getString("calibrationDate")

            cameraMatrix = matrix
            distortionCoeffs = coeffs

            true
        } catch (e: Exception) {
            Log.e("CalibrationManager", "Failed to load calibration", e)
            false
        }
    }

    /**
     * Estimates real-world length from pixel length using focal length.
     */
    fun getScaleFactor(pixelLength: Double): Double {
        val focalLength = cameraMatrix?.get(0, 0)?.firstOrNull() ?: return 0.0
        val mmPerPixel = 1.0 / focalLength
        return pixelLength * mmPerPixel
    }

    /**
     * Detects Charuco pattern and refines corners.
     */
    fun detectCharucoPattern(image: Mat): Pair<Mat, Mat> {
        val dictionary = Aruco.getPredefinedDictionary(Aruco.DICT_6X6_250)
        val corners = ArrayList<Mat>()
        val ids = Mat()
        Aruco.detectMarkers(image, dictionary, corners, ids)

        val charucoCorners = Mat()
        val charucoIds = Mat()
        if (ids.total() > 0) {
            val board = CharucoBoard.create(5, 7, 0.04f, 0.02f, dictionary)
            Aruco.interpolateCornersCharuco(corners, ids, image, board, charucoCorners, charucoIds)

            if (charucoCorners.total() > 0) {
                val gray = Mat()
                Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY)
                val term = TermCriteria(TermCriteria.EPS or TermCriteria.MAX_ITER, 30, 0.1)
                val corners2f = MatOfPoint2f(*charucoCorners.toArray())
                Imgproc.cornerSubPix(gray, corners2f, Size(5.0, 5.0), Size(-1.0, -1.0), term)
                charucoCorners.fromArray(*corners2f.toArray())
            }
        }
        return Pair(charucoCorners, charucoIds)
    }

    /**
     * Generates camera matrix from Charuco detections.
     */
    fun generateCalibrationMatrix(
        charucoCorners: List<Mat>,
        charucoIds: List<Mat>,
        imageSize: Size,
    ): Mat {
        val cameraMatrix = Mat()
        val distCoeffs = Mat()
        val rvecs = ArrayList<Mat>()
        val tvecs = ArrayList<Mat>()
        val board = CharucoBoard.create(5, 7, 0.04f, 0.02f, Aruco.getPredefinedDictionary(Aruco.DICT_6X6_250))

        Aruco.calibrateCameraCharuco(
            charucoCorners,
            charucoIds,
            board,
            imageSize,
            cameraMatrix,
            distCoeffs,
            rvecs,
            tvecs
        )
        return cameraMatrix
    }

    /**
     * Saves calibration data to JSON file.
     */
    fun saveCalibrationToJson(
        cameraMatrix: Mat,
        distortionCoeffs: Mat,
        resolution: Pair<Int, Int>,
    ) {
        val json = JSONObject()
        json.put("cameraMatrix", JSONArray((0 until 3).map { i ->
            JSONArray((0 until 3).map { j -> cameraMatrix.get(i, j)[0] })
        }))
        json.put("distortionCoeffs", JSONArray((0 until distortionCoeffs.cols()).map { i ->
            distortionCoeffs.get(0, i)[0]
        }))
        json.put("resolution", JSONArray(listOf(resolution.first, resolution.second)))
        json.put("calibrationDate", SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()))
        calibrationFile.writeText(json.toString())
    }

    /**
     * Generates calibration matrix from multiple Charuco images.
     */
    fun generateCalibrationMatrixFromImages(images: List<Mat>): Mat {
        val allCorners = ArrayList<Mat>()
        val allIds = ArrayList<Mat>()
        val imageSize = images[0].size()
        for (image in images) {
            val (corners, ids) = detectCharucoPattern(image)
            if (corners.total() > 0 && ids.total() > 0
