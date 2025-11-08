package com.example.celestik.utils

import android.content.Context
import com.example.celestik.models.DetectionItem
import com.google.gson.Gson
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter

/**
 * Provides functions to export detection data into PDF, CSV, Word, and JSON formats.
 * All labels are currently hardcoded in Spanish and should be localized via LocalizedStrings.
 */
object ReportGenerator {

    /**
     * Generates a PDF report from a list of detections.
     *
     * @param context Application context.
     * @param detections List of DetectionItem to include.
     * @param loteId Identifier for the batch.
     * @return File object pointing to the generated PDF.
     */
    fun generatePdfFromDetections(
        context: Context,
        detections: List<DetectionItem>,
        loteId: String,
    ): File {
        val file = File(context.getExternalFilesDir(null), "ReporteCelestic_$loteId.pdf")
        val writer = PdfWriter(file)
        val pdf = com.itextpdf.kernel.pdf.PdfDocument(writer)
        val document = Document(pdf)

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
     *
     * @param context Application context.
     * @param detections List of DetectionItem to include.
     * @param loteId Identifier for the batch.
     * @return File object pointing to the generated CSV.
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
     *
     * @param context Application context.
     * @param detections List of DetectionItem to include.
     * @param loteId Identifier for the batch.
     * @return File object pointing to the generated DOCX.
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
     *
     * @param context Application context.
     * @param detections List of DetectionItem to include.
     * @param loteId Identifier for the batch.
     * @return File object pointing to the generated JSON.
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
