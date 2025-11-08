package com.example.celestik.utils

import android.content.Context
import android.provider.DocumentsContract
import androidx.compose.ui.text.Paragraph
import com.example.celestik.models.DetectionItem
import com.example.celestik.models.enums.DetectionStatus
import com.google.gson.Gson
import com.itextpdf.kernel.pdf.PdfWriter
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter

/**
 * Provides utilities to generate and export detection reports in multiple formats.
 */
object ReportGenerator {

    /**
     * Filters a list of detections by a specific status.
     *
     * @param detections List of DetectionItem to filter.
     * @param status DetectionStatus to match.
     * @return Filtered list of DetectionItem.
     */
    fun filterDetectionsByStatus(
        detections: List<DetectionItem>,
        status: DetectionStatus,
    ): List<DetectionItem> = detections.filter { it.status == status }

    /**
     * Generates a PDF report from a list of detections.
     */
    fun generatePdfFromDetections(
        context: Context,
        detections: List<DetectionItem>,
        loteId: String,
    ): File {
        val file = File(context.getExternalFilesDir(null), "ReporteCelestic_$loteId.pdf")
        val writer = PdfWriter(file)
        val pdf = com.itextpdf.kernel.pdf.PdfDocument(writer)
        val document = DocumentsContract.Document(pdf)

        document.add(Paragraph("Reporte de Detecciones - Lote: $loteId"))
        detections.forEach {
            document.add(Paragraph("ID: ${it.id}"))
            document.add(Paragraph("Tipo: ${it.type}"))
            document.add(Paragraph("Confianza: ${it.confidence}"))
            document.add(Paragraph("Status: ${it.status}"))
            document.add(Paragraph("--------------------"))
        }

        document.close()
        return file
    }

    /**
     * Generates a CSV report from a list of detections.
     */
    fun generateCsvFromDetections(
        context: Context,
        detections: List<DetectionItem>,
        loteId: String,
    ): File {
        val file = File(context.getExternalFilesDir(null), "ReporteCelestic_$loteId.csv")
        val writer = file.bufferedWriter()
        writer.write("ID,Tipo,Confianza,Status,Medida (mm)\n")
        detections.forEach {
            val measurement = it.measurementMm ?: ""
            writer.write("${it.id},${it.type},${it.confidence},${it.status},$measurement\n")
        }
        writer.close()
        return file
    }

    /**
     * Generates a Word report from a list of detections.
     */
    fun generateWordFromDetections(
        context: Context,
        detections: List<DetectionItem>,
        loteId: String,
    ): File {
        val file = File(context.getExternalFilesDir(null), "ReporteCelestic_$loteId.docx")
        val document = XWPFDocument()

        val title = document.createParagraph()
        title.createRun().setText("Reporte de Detecciones - Lote: $loteId")

        detections.forEach {
            val paragraph = document.createParagraph()
            paragraph.createRun().setText("ID: ${it.id}")
            paragraph.createRun().setText("Tipo: ${it.type}")
            paragraph.createRun().setText("Confianza: ${it.confidence}")
            paragraph.createRun().setText("Status: ${it.status}")
            paragraph.createRun().setText("--------------------")
        }

        val fileOut = FileOutputStream(file)
        document.write(fileOut)
        fileOut.close()
        document.close()
        return file
    }

    /**
     * Exports a JSON summary of the detections.
     */
    fun exportJsonSummary(
        context: Context,
        detections: List<DetectionItem>,
        loteId: String
    ): File {
        val gson = Gson()
        val json = gson.toJson(detections)
        val file = File(context.getExternalFilesDir(null), "ReporteCelestic_$loteId.json")
        FileWriter(file).use { it.write(json) }
        return file
    }
}
