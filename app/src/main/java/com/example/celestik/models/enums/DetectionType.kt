package com.example.celestik.models.enums

/**
 * Represents the type of feature or anomaly detected during visual inspection.
 * Includes both presence and absence of expected features.
 */
enum class DetectionType {

    HOLE,                 // Detected hole in the material

    NO_HOLE,              // Expected hole is missing

    COUNTERSINK,          // Detected countersink (conical recess)

    NO_COUNTERSINK,       // Expected countersink is missing

    HALO,                 // Detected halo or ring-like artifact

    NO_HALO,              // Expected halo is missing (e.g., in reflective validation)

    BEND,                 // Detected curvature or deformation

    DEFECT,               // General defect not covered by specific types

    UNKNOWN               // Unclassified or unrecognized feature
}
