package com.example.celestik.models.enums

/**
 * Representa el estado técnico y lógico de una detección,
 * considerando validación dimensional, análisis automático, revisión humana y evolución del defecto.
 */
enum class DetectionStatus {

    OK,                 // 🟢 Dentro de tolerancia, validado automáticamente

    WARNING,            // 🟡 Fuera de tolerancia leve, no crítico

    NOT_ACCEPTED,       // 🔴 Fuera de tolerancia grave o defecto crítico

    CRITICAL,           // 🔴⚠️ Defecto grave que requiere parada o intervención inmediata

    REVIEW,             // 🟠 Requiere revisión manual por técnico o IA no concluyente

    REPAIRED,           // 🔵 Defecto corregido tras intervención

    SKIPPED,            // ⚪ Detección omitida por configuración o exclusión

    PREDICTED,          // 🧠 Estado inferido por modelo de IA (sin validación directa)

    UNCERTAIN           // ❓ Estado no concluyente por baja confianza o ambigüedad
}
