package com.example.celestik.models.report

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.celestik.models.enums.DetectionStatus
import com.example.celestik.models.enums.DetectionType
import kotlinx.parcelize.Parcelize

/**
 * Represents a single entry in an inspection history or generated report.
 * Captures detection metadata, classification, confidence, and optional measurements.
 */
@Parcelize
@Entity(tableName = "report_entries")
data class ReportEntry(

    /**
     * Auto-generated primary key for Room.
     */
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    /**
     * Identifier of the frame or image where the detection occurred.
     */
    @ColumnInfo(name = "frame_id")
    val frameId: String,

    /**
     * Timestamp of the detection in epoch milliseconds.
     */
    @ColumnInfo(name = "timestamp")
    val timestamp: Long,

    /**
     * Type of feature detected (e.g., HOLE, BEND, NO_HALO).
     */
    @ColumnInfo(name = "detected_type")
    val type: DetectionType,

    /**
     * Technical status of the detection (e.g., OK, WARNING, NOT_ACCEPTED).
     */
    @ColumnInfo(name = "status")
    val status: DetectionStatus,

    /**
     * Confidence score of the detection (range: 0.0 to 1.0).
     */
    @ColumnInfo(name = "confidence")
    val confidence: Float,

    /**
     * Optional measurement in millimeters, if applicable.
     */
    @ColumnInfo(name = "measurement_mm")
    val measurementMm: Float?,

    /**
     * Optional notes or annotations related to the detection.
     */
    @ColumnInfo(name = "notes")
    val notes: String = ""

) : Parcelable
