package com.example.celestik.manager

import android.content.Context
import android.os.Build
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import org.opencv.aruco.Aruco
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import org.opencv.objdetect.CharucoBoard
import java.io.File
import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

/**
 * Manages camera calibration using Charuco boards and OpenCV.
 * Loads and saves calibration data, and computes scale factors and matrices.
 * Supports device-specific calibration profiles based on hardware metadata.
 */
class CalibrationManager @Inject constructor(private val context: Context) {

    var cameraMatrix: Mat? = null
    var distortionCoeffs: Mat? = null
    var resolution: Pair<Int, Int>? = null
    var calibrationDate: String? = null

    // Metadata variables for device identification
    var deviceModel: String = Build.MODEL
    var deviceManufacturer: String = Build.MANUFACTURER

    private val calibrationFile = File(context.filesDir, "config/calibration.json").apply {
        parentFile?.mkdirs()
    }

    init {
        // Attempt to load manual calibration first, fallback to device defaults
        if (!loadCalibration()) {
            loadDefaultDeviceCalibration()
        }
    }

    /**
     * Loads calibration data from a JSON file stored in the app's internal storage.
     */
    private fun loadCalibration(): Boolean {
        if (!calibrationFile.exists()) return false
        return try {
            val json = JSONObject(FileInputStream(calibrationFile).bufferedReader().use { it.readText() })
            parseJsonToCalibration(json)
            true
        } catch (e: Exception) {
            Log.e("CalibrationManager", "Failed to load manual calibration", e)
            false
        }
    }

    /**
     * Fallback mechanism: Loads predefined parameters for specific hardware.
     */
    private fun loadDefaultDeviceCalibration() {
        Log.d("CalibrationManager", "Loading default profile for $deviceManufacturer $deviceModel")

        when {
            deviceModel.contains("Pixel 6") -> {
                setupCalibration(
                    matrix = doubleArrayOf(1120.0, 0.0, 640.0, 0.0, 1120.0, 360.0, 0.0, 0.0, 1.0),
                    dist = doubleArrayOf(0.1, -0.04, 0.0, 0.0, 0.0),
                    res = Pair(1280, 720)
                )
            }
            deviceManufacturer.contains("Samsung", ignoreCase = true) -> {
                setupCalibration(
                    matrix = doubleArrayOf(1150.0, 0.0, 640.0, 0.0, 1150.0, 360.0, 0.0, 0.0, 1.0),
                    dist = doubleArrayOf(0.12, -0.06, 0.0, 0.0, 0.0),
                    res = Pair(1280, 720)
                )
            }
            else -> {
                setupCalibration(
                    matrix = doubleArrayOf(1123.5, 0.0, 640.0, 0.0, 1123.5, 360.0, 0.0, 0.0, 1.0),
                    dist = doubleArrayOf(0.11, -0.05, 0.0, 0.0, 0.0),
                    res = Pair(1280, 720)
                )
            }
        }
        calibrationDate = "Default-Profile"
    }

    private fun setupCalibration(matrix: DoubleArray, dist: DoubleArray, res: Pair<Int, Int>) {
        cameraMatrix = Mat(3, 3, CvType.CV_64F).apply { put(0, 0, *matrix) }
        distortionCoeffs = Mat(1, dist.size, CvType.CV_64F).apply { put(0, 0, *dist) }
        resolution = res
    }

    private fun parseJsonToCalibration(json: JSONObject) {
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
        calibrationDate = json.optString("calibrationDate", "Unknown")
        deviceModel = json.optString("deviceModel", Build.MODEL)
        deviceManufacturer = json.optString("deviceManufacturer", Build.MANUFACTURER)

        cameraMatrix = matrix
        distortionCoeffs = coeffs
    }

    fun getScaleFactor(pixelLength: Double): Double {
        val focalLength = cameraMatrix?.get(0, 0)?.get(0) ?: return 0.0
        return pixelLength * (1.0 / focalLength)
    }

    /**
     * Detects Charuco pattern. Handles both color and grayscale input.
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
                // cornerSubPix requires grayscale image
                val gray = Mat()
                if (image.channels() > 1) {
                    Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY)
                } else {
                    image.copyTo(gray)
                }

                val term = TermCriteria(TermCriteria.EPS or TermCriteria.MAX_ITER, 30, 0.1)

                // Convert to MatOfPoint2f for valid API access
                val corners2f = MatOfPoint2f()
                charucoCorners.copyTo(corners2f)

                Imgproc.cornerSubPix(gray, corners2f, Size(5.0, 5.0), Size(-1.0, -1.0), term)

                // Copy back to generic Mat if necessary, or just keep as corners2f
                corners2f.copyTo(charucoCorners)

                gray.release()
                corners2f.release()
            }
        }
        return Pair(charucoCorners, charucoIds)
    }

    fun saveCalibrationToJson(
        cameraMatrix: Mat,
        distortionCoeffs: Mat,
        resolution: Pair<Int, Int>,
    ) {
        val json = JSONObject().apply {
            put("cameraMatrix", JSONArray((0 until 3).map { i ->
                JSONArray((0 until 3).map { j -> cameraMatrix.get(i, j)[0] })
            }))
            put("distortionCoeffs", JSONArray((0 until distortionCoeffs.cols()).map { i ->
                distortionCoeffs.get(0, i)[0]
            }))
            put("resolution", JSONArray(listOf(resolution.first, resolution.second)))
            put("calibrationDate", SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()))
            put("deviceModel", Build.MODEL)
            put("deviceManufacturer", Build.MANUFACTURER)
        }
        calibrationFile.writeText(json.toString())
    }

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
}
