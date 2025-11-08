package com.example.celestik.models.calibration

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 * Represents camera calibration parameters obtained using a Charuco board.
 * This entity is stored in Room for reuse and validation across sessions.
 */
@Parcelize
@Entity(tableName = "camera_calibration")
data class CameraCalibrationData(

    /**
     * Auto-generated primary key for database storage.
     */
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    /**
     * Camera matrix serialized as a JSON string (3x3 matrix from OpenCV).
     */
    @ColumnInfo(name = "camera_matrix")
    val cameraMatrix: String,

    /**
     * Distortion coefficients serialized as a JSON string (typically 5–8 values).
     */
    @ColumnInfo(name = "distortion_coeffs")
    val distortionCoeffs: String,

    /**
     * Image resolution width used during calibration.
     */
    @ColumnInfo(name = "resolution_width")
    val resolutionWidth: Int,

    /**
     * Image resolution height used during calibration.
     */
    @ColumnInfo(name = "resolution_height")
    val resolutionHeight: Int,

    /**
     * Timestamp of calibration in format "YYYY-MM-DD HH:mm".
     */
    @ColumnInfo(name = "calibration_date")
    val calibrationDate: String

) : Parcelable
