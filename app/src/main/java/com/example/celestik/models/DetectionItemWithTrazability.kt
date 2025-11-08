package com.example.celestik.models

/**
 * Combines a DetectionItem with optional traceability metadata.
 * Used for enriched reporting, filtering, or traceable inspection workflows.
 */
data class DetectionItemWithTraceability(
    val detectionItem: DetectionItem,
    val traceability: TraceabilityItem?
)

/**
 * Extension function to attach traceability metadata to a DetectionItem.
 */
fun DetectionItem.withTraceability(info: TraceabilityItem?): DetectionItemWithTraceability {
    return DetectionItemWithTraceability(this, info)
}
