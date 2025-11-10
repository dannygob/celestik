package com.example.celestik.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Represents traceability metadata associated with a detection or inspection.
 * Used for enriched reporting, filtering, and audit trails.
 */
@Parcelize
data class TraceabilityItem(

    /**
     * Unique traceability code (e.g., part ID, batch code).
     */
    val code: String,

    /**
     * Number or label of pieces inspected or affected.
     */
    val pieces: String,

    /**
     * Operator or technician responsible for the inspection.
     */
    val operator: String,

    /**
     * Date of inspection or traceability record (format: "YYYY-MM-DD HH:mm").
     */
    val Date: String,

    /**
     * Summary of inspection results or status.
     */
    val results: String,

    val Pieces: String,


    ) : Parcelable
