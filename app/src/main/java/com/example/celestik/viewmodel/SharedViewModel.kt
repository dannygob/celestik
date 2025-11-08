package com.example.celestik.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Enum representing the type of marker used for detection.
 * Extendable for future marker types (e.g., QR, custom).
 */
enum class MarkerType {
    ARUCO,
    APRILTAG
}

/**
 * SharedViewModel holds UI-related shared state across screens,
 * such as unit preferences and marker type selection.
 */
class SharedViewModel : ViewModel() {

    // Indicates whether the user prefers inches instead of metric units.
    private val _useInches = MutableStateFlow(false)
    val useInches: StateFlow<Boolean> get() = _useInches

    // Stores the currently selected marker type for detection.
    private val _markerType = MutableStateFlow(MarkerType.ARUCO)
    val markerType: StateFlow<MarkerType> get() = _markerType

    /**
     * Updates the unit preference (inches vs metric).
     */
    fun setUseInches(useInches: Boolean) {
        _useInches.value = useInches
    }

    /**
     * Updates the selected marker type.
     */
    fun setMarkerType(markerType: MarkerType) {
        _markerType.value = markerType
    }
}
