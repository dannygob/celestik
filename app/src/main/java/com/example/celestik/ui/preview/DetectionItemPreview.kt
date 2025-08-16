package com.example.celestik.ui.preview

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.celestik.models.DetectionItem
import com.example.celestik.models.enums.DetectionStatus
import com.example.celestik.models.enums.DetectionType
import com.example.celestik.models.geometry.BoundingBox

@Composable
fun DetectionItemPreview() {
    val item = DetectionItem(
        frameId = "frame_007",
        type = DetectionType.COUNTERSINK,
        boundingBox = BoundingBox(12f, 34f, 56f, 78f),
        confidence = 0.88f,
        status = DetectionStatus.WARNING,
        measurementMm = 4.2f,
        timestamp = System.currentTimeMillis(),
        linkedQrCode = "QR-L123",
        notes = "Preview de prueba",
        id = TODO(),
        inspectionId = TODO()
    )

    Text(text = "⚙️ Preview: ${item.type} - ${item.measurementMm}mm - ${item.status}")
}