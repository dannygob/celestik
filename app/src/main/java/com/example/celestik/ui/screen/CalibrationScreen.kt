package com.example.celestik.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

/**
 * Displays the calibration screen with a title and a button to initiate manual calibration.
 *
 * @param navController Navigation controller for screen transitions.
 */
@Composable
fun CalibrationScreen(navController: NavController) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.openCalibration),
            style = MaterialTheme.typography.titleLarge
        )

        Button(onClick = {
            Toast.makeText(context, "Calibrating...", Toast.LENGTH_SHORT).show()
            // TODO: Trigger actual calibration logic or navigate to calibration flow
        }) {
            Text("Iniciar calibración")
        }
    }
}
