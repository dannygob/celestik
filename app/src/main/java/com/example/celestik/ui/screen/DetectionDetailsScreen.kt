package com.example.celestik.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

/**
 * Placeholder screen for displaying detection details.
 * Currently only shows the detection ID.
 *
 * @param navController Navigation controller for routing.
 * @param detectionId Optional ID of the detection to display.
 */
@Composable
fun DetectionDetailsScreen(navController: NavController, detectionId: String?) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Details for detection: $detectionId")
    }
}
