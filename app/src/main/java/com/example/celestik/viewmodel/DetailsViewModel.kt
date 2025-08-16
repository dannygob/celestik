package com.example.celestik.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.celestik.data.repository.DetectionRepository
import com.example.celestik.models.TrazabilityItem
import com.example.celestik.models.calibration.DetectedFeature
import com.example.celestik.utils.Result
import com.example.celestik.utils.buscarPorCodigo
import com.example.celestik.utils.cargarTrazabilidadDesdeJson
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

    private val _trazabilityItem = MutableStateFlow<Result<TrazabilityItem?>>(Result.Loading)
    val trazabilityItem: StateFlow<Result<TrazabilityItem?>> = _trazabilityItem

    private val _features =
        MutableStateFlow<List<DetectedFeature>>(emptyList())
    val features: StateFlow<List<DetectedFeature>> =
        _features

    fun loadTrazabilidad(codigo: String) {
        viewModelScope.launch {
            try {
                val lista = cargarTrazabilidadDesdeJson(context)
                _trazabilityItem.value = Result.Success(buscarPorCodigo(codigo, lista))
            } catch (e: Exception) {
                _trazabilityItem.value = Result.Error(e)
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
