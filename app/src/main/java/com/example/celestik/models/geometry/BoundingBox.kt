package com.example.celestik.models.geometry

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Represents the bounding area of a detected feature in relative coordinates.
 * Coordinates may be in pixels or normalized scale depending on context.
 */
@Parcelize
data class BoundingBox(

    /**
     * Left (X) coordinate of the bounding box.
     */
    val left: Float,

    /**
     * Top (Y) coordinate of the bounding box.
     */
    val top: Float,

    /**
     * Right (X) coordinate of the bounding box.
     */
    val right: Float,

    /**
     * Bottom (Y) coordinate of the bounding box.
     */
    val bottom: Float

) : Parcelable {

    /**
     * Returns the width of the bounding box.
     */
    fun width(): Float = right - left

    /**
     * Returns the height of the bounding box.
     */
    fun height(): Float = bottom - top
}
