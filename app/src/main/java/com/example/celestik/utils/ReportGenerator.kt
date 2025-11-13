package com.example.celestik.utils

import android.content.Context
import com.example.celestik.models.DetectionItem
import com.example.celestik.models.enums.DetectionStatus
import com.example.celestik.models.MeasurementSet
import com.google.gson.Gson
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter

/**
 * Utilidades para generar reportes de detecciones en formatos PDF, Word, CSV y JSON.
 */
object ReportGenerator {

    private fun getReportDirectory(): File {
        val dir = File("/storage/emulated/0/Celestik/Reports/")
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    fun filterDetectionsByStatus(
        detections: List<DetectionItem>,
        status: DetectionStatus,
    ): List<DetectionItem> = detections.filter { it.status == status }

    fun generatePdfFromDetections(
        context: Context,
        detections: List<DetectionItem>,
        loteId: String,
    ): File {
        val dir = getReportDirectory()
        val file = File(dir, "ReporteCelestic_$loteId.pdf")
        val writer = PdfWriter(file)
        val pdfDoc = PdfDocument(writer)
        val document = Document(pdfDoc)

        document.add(Paragraph("Reporte de Detecciones - Lote: $loteId"))
        detections.forEach {
            document.add(Paragraph("ID: ${it.id}"))
            document.add(Paragraph("Tipo: ${it.type}"))
            document.add(Paragraph("Confianza: ${it.confidence}"))
            document.add(Paragraph("Status: ${it.status}"))
            document.add(Paragraph("Medidas: ${formatMeasurements(it.measurements)}"))
            document.add(Paragraph("Notas: ${it.notes}"))
            document.add(Paragraph("--------------------"))
        }

        document.close()
        return file
    }

    fun generateCsvFromDetections(
        context: Context,
        detections: List<DetectionItem>,
        loteId: String,
    ): File {
        val dir = getReportDirectory()
        val file = File(dir, "ReporteCelestic_$loteId.csv")
        val writer = file.bufferedWriter()
        writer.write("ID,Tipo,Confianza,Status,Medidas,Notas
")
        detections.forEach {
            val medidas = formatMeasurements(it.measurements)
            writer.write("${it.id},${it.type},${it.confidence},${it.status},"$medidas","${it.notes}"
")
        }
        writer.close()
        return file
    }

    fun generateWordFromDetections(
        context: Context,
        detections: List<DetectionItem>,
        loteId: String,
    ): File {
        val dir = getReportDirectory()
        val file = File(dir, "ReporteCelestic_$loteId.docx")
        val document = XWPFDocument()

        val title = document.createParagraph()
        title.createRun().setText("Reporte de Detecciones - Lote: $loteId")

        detections.forEach {
            val paragraph = document.createParagraph()
            paragraph.createRun().setText("ID: ${it.id}")
            paragraph.createRun().addBreak()
            paragraph.createRun().setText("Tipo: ${it.type}")
            paragraph.createRun().addBreak()
            paragraph.createRun().setText("Confianza: ${it.confidence}")
            paragraph.createRun().addBreak()
            paragraph.createRun().setText("Status: ${it.status}")
            paragraph.createRun().addBreak()
            paragraph.createRun().setText("Medidas: ${formatMeasurements(it.measurements)}")
            paragraph.createRun().addBreak()
            paragraph.createRun().setText("Notas: ${it.notes}")
            paragraph.createRun().addBreak()
            paragraph.createRun().setText("--------------------")
        }

        val fileOut = FileOutputStream(file)
        document.write(fileOut)
        fileOut.close()
        document.close()
        return file
    }

    fun exportJsonSummary(
        context: Context,
        detections: List<DetectionItem>,
        loteId: String
    ): File {
        val dir = getReportDirectory()
        val gson = Gson()
        val json = gson.toJson(detections)
        val file = File(dir, "ReporteCelestic_$loteId.json")
        FileWriter(file).use { it.write(json) }
        return file
    }

    private fun formatMeasurements(measurements: MeasurementSet?): String {
        if (measurements == null) return ""
        val parts = mutableListOf<String>()
        measurements.diameter?.let { parts.add("Diámetro: $it mm") }
        measurements.depth?.let { parts.add("Profundidad: $it mm") }
        measurements.angle?.let { parts.add("Ángulo: $it°") }
        measurements.radiusInternal?.let { parts.add("Radio Interno: $it mm") }
        measurements.radiusExternal?.let { parts.add("Radio Externo: $it mm") }
        measurements.length?.let { parts.add("Longitud: $it mm") }
        measurements.width?.let { parts.add("Ancho: $it mm") }
        return parts.joinToString("; ")
    }
}
