package com.example.celestik.data.repository

import com.example.celestik.data.dao.CelestikDao
import com.example.celestik.models.DetectionItem
import com.example.celestik.models.Inspection
import com.example.celestik.models.calibration.CameraCalibrationData
import com.example.celestik.models.calibration.DetectedFeature
import com.example.celestik.models.report.ReportConfig
import kotlinx.coroutines.flow.Flow

class DetectionRepository(private val dao: CelestikDao) {

    suspend fun saveDetection(detection: DetectedFeature) {
        dao.insertDetection(detection)
    }

    suspend fun saveDetections(detections: List<DetectedFeature>) {
        dao.insertDetections(detections)
    }

    fun loadDetections(): Flow<List<DetectedFeature>> {
        return dao.getAllDetections()
    }

    suspend fun clearAllDetections() {
        dao.clearDetections()
    }

    suspend fun insertDetection(item: DetectionItem) = dao.insert(item)

    suspend fun deleteDetection(item: DetectionItem) = dao.delete(item)

    suspend fun insertCameraCalibrationData(cameraCalibrationData: CameraCalibrationData) {
        dao.insertCameraCalibrationData(cameraCalibrationData)
    }

    fun getCameraCalibrationData(): Flow<CameraCalibrationData?> {
        return dao.getCameraCalibrationData()
    }

    suspend fun insertReportConfig(reportConfig: ReportConfig) {
        dao.insertReportConfig(reportConfig)
    }

    fun getReportConfig(): Flow<ReportConfig?> {
        return dao.getReportConfig()
    }

    fun getAll(): Flow<List<DetectionItem>> {
        return dao.getAll()
    }

    fun getFeaturesForDetection(detectionItemId: Long): Flow<List<DetectedFeature>> {
        return dao.getFeaturesForDetection(detectionItemId)
    }

    suspend fun startInspection(): Long {
        val inspection =
            Inspection(timestamp = System.currentTimeMillis())
        return dao.insertInspection(inspection)
    }

    fun getAllInspections(): Flow<List<Inspection>> {
        return dao.getAllInspections()
    }
}