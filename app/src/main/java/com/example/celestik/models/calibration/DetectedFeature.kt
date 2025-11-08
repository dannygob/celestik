package com.example.celestik.models.calibration

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 * Represents a visual feature detected in a calibrated image.
 * Used for storing feature metadata such as type, position, confidence, and measurements.
 */
@Parcelize
@Entity(tableName = "detected_features")
data class DetectedFeature(

    /**
     * Auto-generated primary key for Room.
     */
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    /**
     * Foreign key linking this feature to a DetectionItem.
     */
    @ColumnInfo(name = "detection_item_id")
    val detectionItemId: Long,

    /**
     * Type of feature detected (e.g., "charuco_corner", "aruco_marker", "edge").
     */
    @ColumnInfo(name = "feature_type")
    val featureType: String,

    /**
     * X coordinate of the feature in image space.
     */
    @ColumnInfo(name = "x_coord")
    val xCoord: Float,

    /**
     * Y coordinate of the feature in image space.
     */
    @ColumnInfo(name = "y_coord")
    val yCoord: Float,

    /**
     * Confidence score of the detection (range: 0.0 to 1.0).
     */
    @ColumnInfo(name = "confidence")
    val confidence: Float,

    /**
     * Timestamp of detection in epoch milliseconds.
     */
    @ColumnInfo(name = "timestamp")
    val timestamp: Long,

    /**
     * Optional measurements associated with the feature (e.g., length, angle).
     * Requires a Room TypeConverter for Map<String, Float>.
     */
    @ColumnInfo(name = "measurements")
    val measurements: Map<String, Float> = emptyMap()

) : Parcelable
