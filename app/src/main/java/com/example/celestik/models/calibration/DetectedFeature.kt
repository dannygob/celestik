package com.example.celestik.models.calibration

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 * Represents a visual feature detected in a calibrated image.
 */
@Parcelize
@Entity(tableName = "detected_features")
data class DetectedFeature(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "detection_item_id")
    val detectionItemId: Long,

    @ColumnInfo(name = "feature_type")
    val featureType: String, // Ex: "charuco_corner", "aruco_marker", "edge", etc.

    @ColumnInfo(name = "x_coord")
    val xCoord: Float,

    @ColumnInfo(name = "y_coord")
    val yCoord: Float,

    @ColumnInfo(name = "confidence")
    val confidence: Float, // Detection certainty value, 0.0 - 1.0

    @ColumnInfo(name = "timestamp")
    val timestamp: Long, // Epoch time en milisegundos

    @ColumnInfo(name = "measurements")
    val measurements: Map<String, Float> = emptyMap(),
) : Parcelable
