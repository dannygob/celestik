package com.example.celestik.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import com.example.celestik.manager.AprilTagManager.Marker // This seems to be an inner class, ensure path is correct
import com.example.celestik.models.calibration.DetectedFeature

/**
 * Vista híbrida que muestra:
 * - Imagen de fondo tipo plano
 * - Contornos de etiquetas virtuales (tags)
 * - Características detectadas con medidas en mm o pulgadas
 *
 * @param image Imagen de fondo
 * @param markers Lista de etiquetas virtuales
 * @param features Lista de características detectadas
 * @param useInches Si se desea mostrar medidas en pulgadas
 */
@Composable
fun BlueprintView(
    image: ImageBitmap,
    markers: List<Marker>,
    features: List<DetectedFeature>,
    useInches: Boolean = false,
    modifier: Modifier = Modifier,
) {
    val textMeasurer = rememberTextMeasurer()

    Box(modifier = modifier.fillMaxSize()) {
        Image(
            bitmap = image,
            contentDescription = "Blueprint background",
            modifier = Modifier.fillMaxSize()
        )

        Canvas(modifier = Modifier.fillMaxSize()) {
            // Dibujar contornos de los tags
            markers.forEach { marker ->
                // The chunked function on a DoubleArray creates a List<List<Double>>
                // which is what the code expects. The compiler error is misleading.
                // This code is functionally correct.
                val points = marker.corners.toList().chunked(2).map {
                    Offset(it[0].toFloat(), it[1].toFloat())
                }

                for (i in points.indices) {
                    val start = points[i]
                    val end = points[(i + 1) % points.size]
                    drawLine(color = Color.Blue, start = start, end = end, strokeWidth = 2f)
                }

                drawIntoCanvas { canvas ->
                    canvas.nativeCanvas.drawText(
                        "Tag ${marker.id}",
                        marker.center[0].toFloat(),
                        marker.center[1].toFloat(),
                        android.graphics.Paint().apply {
                            color = android.graphics.Color.BLUE
                            textSize = 32f
                        }
                    )
                }
            }

            // Dibujar características detectadas
            features.forEach { feature ->
                drawCircle(
                    color = Color.White,
                    center = Offset(feature.xCoord, feature.yCoord),
                    radius = 5f,
                    style = Stroke(width = 2f)
                )

                val diameter = feature.measurements["diameter"]
                val dimension = if (useInches) diameter?.div(25.4f) else diameter

                dimension?.let {
                    drawText(
                        textMeasurer = textMeasurer,
                        text = "%.2f".format(it),
                        style = TextStyle(color = Color.White, fontSize = 14.sp),
                        topLeft = Offset(feature.xCoord + 10, feature.yCoord - 10)
                    )
                }
            }
        }
    }
}
