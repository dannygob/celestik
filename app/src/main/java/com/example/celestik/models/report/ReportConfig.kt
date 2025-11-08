package com.example.celestik.models.report

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 * Defines the configuration for report export in Celestik.
 * Stores user preferences such as format, metadata inclusion, and generation options.
 */
@Parcelize
@Entity(tableName = "report_config")
data class ReportConfig(

    /**
     * Auto-generated primary key for Room.
     */
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    /**
     * Title of the report to be generated.
     */
    @ColumnInfo(name = "report_title")
    val reportTitle: String,

    /**
     * Whether to include metadata in the report (e.g., calibration info, timestamps).
     */
    @ColumnInfo(name = "include_metadata")
    val includeMetadata: Boolean,

    /**
     * Whether to include raw feature data (e.g., detected points, bounding boxes).
     */
    @ColumnInfo(name = "include_raw_features")
    val includeRawFeatures: Boolean,

    /**
     * Format used for internal data export (e.g., "CSV", "JSON").
     */
    @ColumnInfo(name = "export_format")
    val exportFormat: String,

    /**
     * Timestamp of report generation in format "YYYY-MM-DD HH:mm".
     */
    @ColumnInfo(name = "generation_date")
    val generationDate: String,

    /**
     * Whether to include images in the report output.
     */
    @ColumnInfo(name = "include_images")
    val includeImages: Boolean,

    /**
     * Final output format of the report (e.g., "PDF", "Word", "JSON").
     */
    @ColumnInfo(name = "output_format")
    val outputFormat: String,

    /**
     * Whether to include measurement data (e.g., lengths, angles).
     */
    @ColumnInfo(name = "include_measurements")
    val includeMeasurements: Boolean

) : Parcelable
