package com.example.celestik.models

/**
 * Conjunto de medidas técnicas asociadas a una detección.
 */
data class MeasurementSet(
    val diameter: Float? = null,
    val depth: Float? = null,
    val angle: Float? = null,
    val radiusInternal: Float? = null,
    val radiusExternal: Float? = null,
    val length: Float? = null,
    val width: Float? = null
)
