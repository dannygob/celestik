package com.example.celestik.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Displays a measurement label in the top-left corner of the screen.
 * Useful for overlaying dimensional data on visual components.
 *
 * @param value The numeric value to display.
 * @param unit The unit of measurement (default is "mm").
 */
@Composable
fun MeasurementOverlay(value: String, unit: String = "mm") {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Text(
            text = "$value $unit",
            fontSize = 16.sp,
            color = Color.White,
            modifier = Modifier
                .background(Color.Black.copy(alpha = 0.6f))
                .padding(4.dp)
        )
    }
}
