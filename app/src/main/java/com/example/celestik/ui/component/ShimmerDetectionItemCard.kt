package com.example.celestik.ui.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Displays a shimmer placeholder card simulating the layout of a DetectionItemCard.
 * Used while detection data is loading.
 * This version uses the modern Jetpack Compose method for creating a shimmer effect.
 */
@Composable
fun ShimmerDetectionItemCard() {
    // 1. Define the colors for the shimmer gradient.
    // These can be adjusted to match the look you want.
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f)
    )

    // 2. Set up the infinite animation for the gradient's position.
    val transition = rememberInfiniteTransition(label = "ShimmerTransition")
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f, // A large value to ensure the gradient sweeps across the screen
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart // Use Restart for a continuous sweep
        ),
        label = "ShimmerTranslateAnimation"
    )

    // 3. Create the Brush using the colors and animated position.
    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )

    // 4. Apply the brush to the background of your placeholder elements.
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            repeat(3) {
                // This is the corrected version of line 41
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .background(brush = brush) // Pass the Brush object here
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(20.dp)
                    .background(brush = brush) // And here
            )
        }
    }
}
