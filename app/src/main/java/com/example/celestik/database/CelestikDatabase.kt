package com.example.celestik.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.celestik.data.dao.CelestikDao
import com.example.celestik.database.converters.Converters
import com.example.celestik.models.DetectionItem
import com.example.celestik.models.Inspection
import com.example.celestik.models.calibration.CameraCalibrationData
import com.example.celestik.models.calibration.DetectedFeature
import com.example.celestik.models.report.ReportConfig

/**
 * CelestikDatabase defines the Room database configuration.
 * It registers all entities and provides access to the DAO.
 */
@Database(
    entities = [
        DetectionItem::class,
        DetectedFeature::class,
        CameraCalibrationData::class,
        ReportConfig::class,
        Inspection::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class CelestikDatabase : RoomDatabase() {

    /**
     * Exposes the DAO for performing database operations.
     */
    abstract fun celestikDao(): CelestikDao

    companion object {
        @Volatile
        private var INSTANCE: CelestikDatabase? = null

        /**
         * Returns the singleton instance of the database.
         * Uses synchronized block for thread safety.
         */
        fun getDatabase(context: Context): CelestikDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CelestikDatabase::class.java,
                    "celestik_database"
                )
                    // WARNING: This will wipe data if migration is missing.
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
