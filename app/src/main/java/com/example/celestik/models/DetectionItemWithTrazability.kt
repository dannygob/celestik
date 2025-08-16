package com.example.celestik.models


data class DetectionItemConTrazabilidad(
    val detectionItem: DetectionItem,
    val traceability: TraceabilityItem?,
)

fun DetectionItem.conTrazabilidad(info: TraceabilityItem?): DetectionItemConTrazabilidad {
    return DetectionItemConTrazabilidad(this, info)
}