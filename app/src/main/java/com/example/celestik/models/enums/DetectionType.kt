package com.example.celestik.models.enums

/**
 * Represents the type of feature or anomaly detected during visual inspection.
 * Includes both presence and absence of expected features.
 */
enum class DetectionType {

    HOLE,
    NO_HOLE,

    COUNTERSINK,
    NO_COUNTERSINK,

    HALO,
    NO_HALO,

    BEND,
    DEFECT,

    EDGE,               // Arista o borde detectado
    CIRCLE,             // Círculo detectado por Hough
    DEFORMATION,        // Deformación por Optical Flow
    PATTERN_MATCH,      // Coincidencia por Template Matching
    HALO_INNER,         // Halo interno
    HALO_OUTER,         // Halo externo
    SCRATCH,            // Rasguño o línea
    BLOB,               // Masa o agrupación

    UNKNOWN
}
