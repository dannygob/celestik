package com.example.celestik.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.celestik.data.repository.DetectionRepository
import com.example.celestik.models.TraceabilityItem
import com.example.celestik.models.calibration.DetectedFeature
import com.example.celestik.utils.Result
import com.example.celestik.utils.loadTractabilityFromJson
import com.example.celestik.utils.searchForCode
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: DetectionRepository,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _traceabilityItem = MutableStateFlow<Result<TraceabilityItem?>>(Result.Loading)
    val traceabilityItem: StateFlow<Result<TraceabilityItem?>> = _traceabilityItem

    private val _features =
        MutableStateFlow<List<DetectedFeature>>(emptyList())
    val features: StateFlow<List<DetectedFeature>> =
        _features

    fun loadTrazabilidad(codigo: String) {
        viewModelScope.launch {
            try {
                val lista = loadTractabilityFromJson(context)
                _traceabilityItem.value = Result.Success(searchForCode(codigo, lista))
            } catch (e: Exception) {
                _traceabilityItem.value = Result.Error(e)
            }
        }
    }

    fun loadFeatures(detectionItemId: Long) {
        viewModelScope.launch {
            repository.getFeaturesForDetection(detectionItemId).collect {
                _features.value = it
            }
        }
    }
}
