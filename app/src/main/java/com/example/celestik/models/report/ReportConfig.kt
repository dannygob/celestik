package com.example.celestik.models.report

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.celestik.models.enums.ReportFormat
import kotlinx.parcelize.Parcelize

/**
 * Defines the configuration for report export in Celestik.
 * Stores user preferences such as format, metadata inclusion, and generation options.
 */
@Parcelize
@Entity(tableName = "report_config")
data class ReportConfig(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "report_title")
    val reportTitle: String,

    @ColumnInfo(name = "include_metadata")
    val includeMetadata: Boolean,

    @ColumnInfo(name = "include_raw_features")
    val includeRawFeatures: Boolean,

    @ColumnInfo(name = "export_format")
    val exportFormat: ReportFormat,

    @ColumnInfo(name = "generation_timestamp")
    val generationTimestamp: Long,

    @ColumnInfo(name = "include_images")
    val includeImages: Boolean,

    @ColumnInfo(name = "output_format")
    val outputFormat: ReportFormat,

    @ColumnInfo(name = "include_measurements")
    val includeMeasurements: Boolean

) : Parcelable {
    fun isValid(): Boolean {
        return reportTitle.isNotBlank()
    }
}
