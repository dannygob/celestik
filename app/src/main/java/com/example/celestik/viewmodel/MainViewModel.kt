package com.example.celestik.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.celestik.data.repository.DetectionRepository
import com.example.celestik.models.DetectionItem
import com.example.celestik.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * ViewModel for exposing the list of detections.
 * Uses Hilt for dependency injection and wraps repository data in a Result<StateFlow>.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: DetectionRepository,
) : ViewModel() {

    val detections: StateFlow<Result<List<DetectionItem>>> =
        repository.getAll()
            .map { Result.Success(it) }
            .catch { throwable ->
                emit(Result.Error(throwable as? Exception ?: Exception(throwable)))
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = Result.Loading
            )
}
