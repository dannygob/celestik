package com.example.celestik.data.repository

import com.example.celestik.data.dao.CelestikDao
import com.example.celestik.models.DetectionItem
import com.example.celestik.models.Inspection
import com.example.celestik.models.calibration.CameraCalibrationData
import com.example.celestik.models.calibration.DetectedFeature
import com.example.celestik.models.report.ReportConfig
import kotlinx.coroutines.flow.Flow

/**
 * DetectionRepository provides an abstraction layer over CelestikDao.
 * It handles all data operations related to detections, features, calibration, inspections, and reports.
 */
class DetectionRepository(private val dao: CelestikDao) {

    // Inserts a single detected feature.
    suspend fun insertDetectedFeature(detection: DetectedFeature) {
        dao.insertDetection(detection)
    }

    // Inserts multiple detected features.
    suspend fun insertDetectedFeatures(detections: List<DetectedFeature>) {
        dao.insertDetections(detections)
    }

    // Retrieves all detected features.
    fun getAllDetectedFeatures(): Flow<List<DetectedFeature>> {
        return dao.getAllDetections()
    }

    // Deletes all detected features.
    suspend fun clearAllDetectedFeatures() {
        dao.clearDetections()
    }

    // Inserts a detection item.
    suspend fun insertDetectionItem(item: DetectionItem) {
        dao.insert(item)
    }

    // Deletes a detection item.
    suspend fun deleteDetectionItem(item: DetectionItem) {
        dao.delete(item)
    }

    // Inserts camera calibration data.
    suspend fun insertCameraCalibrationData(cameraCalibrationData: CameraCalibrationData) {
        dao.insertCameraCalibrationData(cameraCalibrationData)
    }

    // Retrieves the latest camera calibration data.
    fun getCameraCalibrationData(): Flow<CameraCalibrationData?> {
        return dao.getCameraCalibrationData()
    }

    // Inserts report configuration.
    suspend fun insertReportConfig(reportConfig: ReportConfig) {
        dao.insertReportConfig(reportConfig)
    }

    // Retrieves the latest report configuration.
    fun getReportConfig(): Flow<ReportConfig?> {
        return dao.getReportConfig()
    }

    // Retrieves all detection items.
    fun getAllDetectionItems(): Flow<List<DetectionItem>> {
        return dao.getAll()
    }

    // Retrieves detected features linked to a specific detection item.
    fun getFeaturesForDetectionItem(detectionItemId: Long): Flow<List<DetectedFeature>> {
        return dao.getFeaturesForDetection(detectionItemId)
    }

    // Starts a new inspection and returns its row ID.
    suspend fun startInspection(): Long {
        val inspection = Inspection(timestamp = System.currentTimeMillis())
        return dao.insertInspection(inspection)
    }

    // Retrieves all inspections.
    fun getAllInspections(): Flow<List<Inspection>> {
        return dao.getAllInspections()
    }
}
