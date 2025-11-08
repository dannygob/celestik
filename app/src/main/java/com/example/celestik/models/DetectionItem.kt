package com.example.celestik.models

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.celestik.models.enums.DetectionStatus
import com.example.celestik.models.enums.DetectionType
import com.example.celestik.models.geometry.BoundingBox
import kotlinx.parcelize.Parcelize

/**
 * Represents a single detection result within an inspection session.
 * Linked to an Inspection entity and includes metadata such as type, confidence, and measurements.
 */
@Parcelize
@Entity(
    tableName = "detection_items",
    foreignKeys = [
        ForeignKey(
            entity = Inspection::class,
            parentColumns = ["id"],
            childColumns = ["inspectionId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DetectionItem(

    /**
     * Auto-generated primary key for Room.
     */
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    /**
     * Foreign key linking to the parent inspection session.
     */
    val inspectionId: Long,

    /**
     * Identifier of the frame or image where the detection occurred.
     */
    val frameId: String,

    /**
     * Type of feature detected (e.g., HOLE, NO_HALO).
     */
    val type: DetectionType,

    /**
     * Bounding box of the detected feature in image coordinates.
     */
    @Embedded
    val boundingBox: BoundingBox,

    /**
     * Confidence score of the detection (range: 0.0 to 1.0).
     */
    val confidence: Float,

    /**
     * Technical status of the detection (e.g., OK, WARNING, NOT_ACCEPTED).
     */
    val status: DetectionStatus,

    /**
     * Optional measurement in millimeters, if applicable.
     */
    val measurementMm: Float? = null,

    /**
     * Timestamp of detection in epoch milliseconds.
     */
    val timestamp: Long,

    /**
     * Optional QR code linked to the detection (e.g., part ID or traceability).
     */
    val linkedQrCode: String? = null,

    /**
     * Optional notes or annotations related to the detection.
     */
    val notes: String = ""

) : Parcelable
