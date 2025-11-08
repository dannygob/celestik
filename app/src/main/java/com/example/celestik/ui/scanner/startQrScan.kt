package com.example.celestik.ui.scanner

import android.graphics.Bitmap
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.objdetect.QRCodeDetector

/**
 * Provides QR code scanning utilities using OpenCV.
 * Converts a Bitmap to Mat and decodes QR content.
 */
object QRScanner {

    /**
     * Starts a QR scan from a Bitmap image.
     *
     * @param bitmap The input image containing a QR code.
     * @return The decoded QR string if found, or null.
     */
    fun startQrScan(bitmap: Bitmap): String? {
        val mat = Mat()
        Utils.bitmapToMat(bitmap, mat)
        return decodeQrCode(mat)
    }

    /**
     * Decodes a QR code from an OpenCV Mat.
     *
     * @param mat The image matrix to scan.
     * @return The decoded QR string if found, or null.
     */
    fun decodeQrCode(mat: Mat): String? {
        val detector = QRCodeDetector()
        return detector.detectAndDecode(mat).takeIf { it.isNotBlank() }
    }
}
