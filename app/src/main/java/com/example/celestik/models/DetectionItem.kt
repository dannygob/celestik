package com.example.celestik.models

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.celestik.models.enums.DetectionStatus
import com.example.celestik.models.enums.DetectionType
import com.example.celestik.models.enums.MeasurementUnit
import com.example.celestik.models.geometry.BoundingBox
import kotlinx.parcelize.Parcelize

/**
 * Representa un resultado de detección dentro de una sesión de inspección.
 * Incluye metadatos técnicos, medidas, trazabilidad y método de análisis.
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

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val inspectionId: Long,

    val frameId: String,

    val type: DetectionType,

    @Embedded
    val boundingBox: BoundingBox,

    val confidence: Float,

    val status: DetectionStatus,

    /**
     * Conjunto de medidas técnicas asociadas a la detección.
     * Ejemplo: diámetro, profundidad, ángulo, radios, etc.
     */
    @Embedded
    val measurements: MeasurementSet? = null,

    /**
     * Unidad de medida utilizada (mm, pulgadas, etc.).
     */
    val unit: MeasurementUnit = MeasurementUnit.MM,

    val timestamp: Long,

    val linkedQrCode: String? = null,

    val notes: String = "",

    /**
     * Método de detección utilizado (Watershed, OpticalFlow, TemplateMatching, etc.).
     */
    val detectionMethod: String? = null,

    /**
     * Ruta de imagen procesada o miniatura asociada.
     */
    val imagePath: String? = null

) : Parcelable
