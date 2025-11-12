package com.example.celestik.models.calibration

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 * Representa una característica visual detectada en una imagen calibrada.
 * Incluye metadatos como tipo, posición, confianza, medidas técnicas y trazabilidad.
 */
@Parcelize
@Entity(tableName = "detected_features")
data class DetectedFeature(

    /** Clave primaria autogenerada para Room. */
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    /** Clave foránea que vincula esta característica con un DetectionItem. */
    @ColumnInfo(name = "detection_item_id")
    val detectionItemId: Long,

    /** Tipo de característica detectada (ej. "hole", "countersink", "threaded_hole", "halo"). */
    @ColumnInfo(name = "feature_type")
    val featureType: String,

    /** Coordenada X en el espacio de imagen. */
    @ColumnInfo(name = "x_coord")
    val xCoord: Float,

    /** Coordenada Y en el espacio de imagen. */
    @ColumnInfo(name = "y_coord")
    val yCoord: Float,

    /** Nivel de confianza de la detección (rango: 0.0 a 1.0). */
    @ColumnInfo(name = "confidence")
    val confidence: Float,

    /** Marca de tiempo de la detección en milisegundos epoch. */
    @ColumnInfo(name = "timestamp")
    val timestamp: Long,

    /** Medidas técnicas asociadas (ej. diámetro, profundidad, ángulo). */
    @ColumnInfo(name = "measurements")
    val measurements: Map<String, Float> = emptyMap(),

    /** ID del tag virtual generado para esta característica (opcional). */
    @ColumnInfo(name = "tag_id")
    val tagId: Int? = null

) : Parcelable
