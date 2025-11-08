package com.example.celestik.manager

import android.content.Context
import android.graphics.Bitmap
import androidx.core.graphics.scale
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel

/**
 * ImageClassifier loads a TFLite model and performs inference on input Bitmaps.
 * It maps predictions to domain-specific feature types.
 */
class ImageClassifier(context: Context) {

    companion object {
        private const val MODEL_FILE_NAME = "mobilenet_v2.tflite"
        private const val INPUT_IMAGE_SIZE = 224
        private const val NUM_CHANNELS = 3
        private const val NUM_CLASSES = 1001
    }

    private val interpreter: Interpreter

    init {
        val assetFileDescriptor = context.assets.openFd(MODEL_FILE_NAME)
        val inputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = assetFileDescriptor.startOffset
        val declaredLength = assetFileDescriptor.declaredLength
        val modelBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
        interpreter = Interpreter(modelBuffer)
    }

    /**
     * Runs inference on the given bitmap and returns the prediction scores.
     */
    fun runInference(bitmap: Bitmap): FloatArray {
        val inputBuffer = convertBitmapToByteBuffer(bitmap)
        val output = Array(1) { FloatArray(NUM_CLASSES) }
        interpreter.run(inputBuffer, output)
        return output[0]
    }

    /**
     * Converts a Bitmap to a normalized ByteBuffer for model input.
     */
    private fun convertBitmapToByteBuffer(image: Bitmap): ByteBuffer {
        val resizedBitmap = image.scale(INPUT_IMAGE_SIZE, INPUT_IMAGE_SIZE)
        val byteBuffer = ByteBuffer.allocateDirect(4 * INPUT_IMAGE_SIZE * INPUT_IMAGE_SIZE * NUM_CHANNELS)
        byteBuffer.order(ByteOrder.nativeOrder())

        for (y in 0 until INPUT_IMAGE_SIZE) {
            for (x in 0 until INPUT_IMAGE_SIZE) {
                val pixel = resizedBitmap.getPixel(x, y)
                val r = ((pixel shr 16) and 0xFF) / 255f
                val g = ((pixel shr 8) and 0xFF) / 255f
                val b = (pixel and 0xFF) / 255f

                byteBuffer.putFloat(r)
                byteBuffer.putFloat(g)
                byteBuffer.putFloat(b)
            }
        }

        return byteBuffer
    }

    /**
     * Maps the prediction result to a domain-specific feature label.
     */
    fun mapPredictionToFeatureType(predictions: FloatArray): String {
        val maxIndex = predictions.indices.maxByOrNull { predictions[it] } ?: return "Unknown class"
        return when (maxIndex) {
            in 0..100 -> "Surface defect"
            in 101..500 -> "Irregular curvature"
            in 501 until NUM_CLASSES -> "No defect"
            else -> "Unknown class"
        }
    }
}
