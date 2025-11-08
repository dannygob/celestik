package com.example.celestik.models.enums

/**
 * Represents the technical status of a detection result based on dimensional validation.
 *
 * - OK: Feature is within tolerance and considered valid.
 * - WARNING: Feature is out of tolerance but not critical.
 * - NOT_ACCEPTED: Feature is invalid or represents a critical defect.
 */
enum class DetectionStatus {

    OK,           // 🟢 Valid — within tolerance

    WARNING,      // 🟡 Warning — out of tolerance but not critical

    NOT_ACCEPTED  // 🔴 Not accepted — invalid or critical defect
}
