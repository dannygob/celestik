package com.example.celestik.ui.screen

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.celestik.models.DetectionItem
import com.example.celestik.ui.component.BlueprintView
import com.example.celestik.utils.Result
import com.example.celestik.viewmodel.DetailsViewModel
import com.example.celestik.viewmodel.SharedViewModel
import com.google.type.Date

/**
 * Displays detailed information about a detection item, including visual features and traceability.
 *
 * @param navController Navigation controller for routing.
 * @param detailType Type of detail view (e.g., "hole", "alodine").
 * @param detectionItem Optional detection item to display.
 * @param detailsViewModel ViewModel for loading traceability and features.
 * @param sharedViewModel Shared app state (e.g., unit preferences).
 */
@Composable
fun DetailsScreen(
    navController: NavController,
    detailType: String,
    detectionItem: DetectionItem? = null,
    detailsViewModel: DetailsViewModel = viewModel(),
    sharedViewModel: SharedViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val title = when (detailType) {
        "hole" -> stringResource(R.string.details_title_hole)
        "alodine" -> stringResource(R.string.details_title_alodine)
        "countersink" -> stringResource(R.string.details_title_countersink)
        else -> "Details"
    }

    LaunchedEffect(detectionItem) {
        detectionItem?.linkedQrCode?.let { code ->
            detailsViewModel.loadTraceability(code)
        }
        detectionItem?.id?.let {
            detailsViewModel.loadFeatures(it)
        }
    }

    val traceabilityResult by detailsViewModel.traceabilityItem.collectAsState()
    val features by detailsViewModel.features.collectAsState()
    val useInches by sharedViewModel.useInches.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = title, style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        BlueprintView(features = features, useInches = useInches)

        Spacer(modifier = Modifier.height(16.dp))

        features.forEach { feature ->
            Text("Feature: ${feature.featureType} at (${feature.xCoord}, ${feature.yCoord})")
        }

        Spacer(modifier = Modifier.height(16.dp))

        AnimatedVisibility(
            visible = traceabilityResult is Result.Success,
            enter = fadeIn(animationSpec = tween(durationMillis = 1000))
        ) {
            val traceability = (traceabilityResult as Result.Success).data
            traceability?.let {
                HorizontalDivider()
                Text("🔍 Traceability:", style = MaterialTheme.typography.titleMedium)
                Text("• Code: ${it.code}")
                Text("• Pieces: ${it.Pieces}")
                Text("• Operator: ${it.operator}")
                Text("• Date: ${it.Date}")
                Text("• Results: ${it.results}")
            } ?: Text("❌ No traceability data available.")
        }

        if (traceabilityResult is Result.Loading) {
            CircularProgressIndicator()
        }

        if (traceabilityResult is Result.Error) {
            Text("❌ Error loading traceability information.")
        }

        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = {
            Toast.makeText(context, stringResource(R.string.report_issue), Toast.LENGTH_SHORT).show()
            // TODO: Implement issue reporting logic
        }) {
            Text(stringResource(R.string.report_issue))
        }
    }
}
