package com.example.celestik.models.report

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.celestik.models.enums.DetectionStatus
import com.example.celestik.models.enums.DetectionType
import com.example.celestik.models.MeasurementSet
import kotlinx.parcelize.Parcelize

/**
 * Representa una entrada individual en el historial de inspección o en un reporte generado.
 * Captura metadatos de detección, clasificación, confianza, medidas y trazabilidad visual.
 */
@Parcelize
@Entity(tableName = "report_entries")
data class ReportEntry(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "frame_id")
    val frameId: String,

    @ColumnInfo(name = "timestamp")
    val timestamp: Long,

    @ColumnInfo(name = "detected_type")
    val type: DetectionType,

    @ColumnInfo(name = "status")
    val status: DetectionStatus,

    @ColumnInfo(name = "confidence")
    val confidence: Float,

    @Embedded
    val measurements: MeasurementSet? = null,

    @ColumnInfo(name = "notes")
    val notes: String = "",

    @ColumnInfo(name = "image_path")
    val imagePath: String? = null,

    @ColumnInfo(name = "x_coord")
    val xCoord: Float? = null,

    @ColumnInfo(name = "y_coord")
    val yCoord: Float? = null,

    @ColumnInfo(name = "detection_method")
    val detectionMethod: String? = null

) : Parcelable
