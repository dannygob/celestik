package com.example.celestik.models


data class DetectionItemConTrazabilidad(
    val detectionItem: DetectionItem,
    val trazabilidad: TrazabilityItem?,
)

fun DetectionItem.conTrazabilidad(info: TrazabilityItem?): DetectionItemConTrazabilidad {
    return DetectionItemConTrazabilidad(this, info)
}