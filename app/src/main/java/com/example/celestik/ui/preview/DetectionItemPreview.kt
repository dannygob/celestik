package com.example.celestik.ui.preview

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.celestik.models.DetectionItem
import com.example.celestik.models.enums.DetectionStatus
import com.example.celestik.models.enums.DetectionType
import com.example.celestik.models.geometry.BoundingBox

/**
 * Displays a simple text preview of a DetectionItem.
 * Intended for testing model instantiation and basic rendering.
 */
@Composable
fun DetectionItemPreview() {
    val item = DetectionItem(
        id = 1L,
        inspectionId = 101L,
        frameId = "frame_007",
        type = DetectionType.COUNTERSINK,
        boundingBox = BoundingBox(12f, 34f, 56f, 78f),
        confidence = 0.88f,
        status = DetectionStatus.WARNING,
        measurements = 4.2f,
        timestamp = System.currentTimeMillis(),
        linkedQrCode = "QR-L123",
        notes = "Preview de prueba"
    )

    Text(text = "⚙️ Preview: ${item.type} - ${item.measurements} mm - ${item.status}")
}

@Preview(showBackground = true)
@Composable
fun PreviewDetectionItemPreview() {
    DetectionItemPreview()
}
