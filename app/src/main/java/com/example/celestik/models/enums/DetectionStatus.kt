package com.example.celestik.models.enums

/**
 * Technical status of the detection based on dimensional validation.
 * 🟢 OK: Within tolerance
 * 🟡 WARNING: Out of tolerance but not critical
 * 🔴 NOT_ACCEPTED: Invalid or critical defect
 */
enum class DetectionStatus {
    OK,           // 🟢 Valid
    WARNING,      // 🟡 Warning
    NOT_ACCEPTED  // 🔴 Not accepted
}
