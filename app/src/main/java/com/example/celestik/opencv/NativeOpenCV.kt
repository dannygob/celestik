package com.example.celestik.opencv

/**
 * NativeOpenCV provides access to native OpenCV functions via JNI.
 * These functions are implemented in native_opencv.cpp and compiled with CMake.
 */
object NativeOpenCV {

    init {
        // Load native library compiled via CMake
        System.loadLibrary("native_opencv")
    }

    /** Returns OpenCV version string */
    external fun getOpenCVVersion(): String

    /** Detects ArUco markers and returns their IDs */
    external fun detectAruco(matAddr: Long): IntArray

    /** Runs DNN inference using ONNX model */
    external fun runDnn(matAddr: Long, modelPath: String): FloatArray

    /** Predicts using SVM model from OpenCV ML */
    external fun predictML(features: FloatArray, modelPath: String): Float

    /** Estimates pose using solvePnP from calib3d */
    external fun estimatePose(
        cornersAddr: Long,
        cameraMatrixAddr: Long,
        distCoeffsAddr: Long,
    ): FloatArray

    /** Detects keypoints using ORB (features2d) */
    external fun countKeypoints(matAddr: Long): Int

    /** Applies denoising filter (photo module) */
    external fun denoiseImage(matAddr: Long): Long

    /** Calculates optical flow using Farneback (video module) */
    external fun calcOpticalFlow(prevAddr: Long, nextAddr: Long): Long

    /** Performs FLANN nearest neighbor search */
    external fun flannSearch(queryAddr: Long, trainAddr: Long): IntArray
}