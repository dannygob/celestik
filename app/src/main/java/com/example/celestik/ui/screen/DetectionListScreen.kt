package com.example.celestik.ui.screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.celestik.ui.component.DetectionItemCard
import com.example.celestik.ui.component.ShimmerDetectionItemCard
import com.example.celestik.utils.Result
import com.example.celestik.viewmodel.MainViewModel

/**
 * Displays a scrollable list of detection items.
 * Shows shimmer placeholders while loading and renders cards on success.
 *
 * @param navController Navigation controller for routing.
 * @param viewModel ViewModel providing detection data.
 */
@Composable
fun DetectionListScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel(),
) {
    val detectionsResult by viewModel.detections.collectAsState()

    LazyColumn {
        when (detectionsResult) {
            is Result.Loading -> {
                items(5) {
                    ShimmerDetectionItemCard()
                }
            }

            is Result.Success -> {
                val detections = (detectionsResult as Result.Success<*>).data
                items(detections) { item ->
                    DetectionItemCard(item = item)
                }
            }

            is Result.Error -> {
                item {
                    Text("❌ Failed to load detections.")
                    // TODO: Add retry button or error visuals
                }
            }
        }
    }
}
