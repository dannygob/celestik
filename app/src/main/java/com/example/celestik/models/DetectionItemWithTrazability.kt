package com.example.celestik.models


data class DetectionItemWithTraceability(
    val detectionItem: DetectionItem,
    val traceability: TraceabilityItem?,
)

fun DetectionItem.withTraceability(info: TraceabilityItem?): DetectionItemWithTraceability {
    return DetectionItemWithTraceability(this, info)
}