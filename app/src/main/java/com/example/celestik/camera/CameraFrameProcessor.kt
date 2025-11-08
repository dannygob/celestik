package com.example.celestik.camera


import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.celestik.opencv.FrameAnalyzer
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

/**
 * Processes camera frames using OpenCV and FrameAnalyzer.
 */
class CameraFrameProcessor(
    private val frameAnalyzer: FrameAnalyzer,
    private val onResult: (FrameAnalyzer.AnalysisResult) -> Unit
) : ImageAnalysis.Analyzer {

    override fun analyze(image: ImageProxy) {
        try {
            val mat = imageToMat(image)
            val result = frameAnalyzer.analyze(mat)
            onResult(result)
        } catch (e: Exception) {
            Log.e("CameraFrameProcessor", "Error processing frame", e)
        } finally {
            image.close()
        }
    }

    /**
     * Converts ImageProxy to OpenCV Mat in BGR format.
     */
    private fun imageToMat(image: ImageProxy): Mat {
        val yBuffer = image.planes[0].buffer
        val ySize = yBuffer.remaining()
        val yData = ByteArray(ySize)
        yBuffer.get(yData)

        val matY = Mat(image.height, image.width, CvType.CV_8UC1)
        matY.put(0, 0, yData)

        val matBGR = Mat()
        Imgproc.cvtColor(matY, matBGR, Imgproc.COLOR_GRAY2BGR)
        return matBGR
    }
}