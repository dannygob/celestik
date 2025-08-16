package com.example.celestik.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.celestik.data.dao.CelesticDao
import com.example.celestik.database.converters.Converters
import com.example.celestik.models.DetectionItem
import com.example.celestik.models.Inspection
import com.example.celestik.models.calibration.CameraCalibrationData
import com.example.celestik.models.calibration.DetectedFeature
import com.example.celestik.models.report.ReportConfig

@Database(
    entities = [DetectionItem::class, DetectedFeature::class, CameraCalibrationData::class, ReportConfig::class, Inspection::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class CelesticDatabase : RoomDatabase() {
    abstract fun celesticDao(): CelesticDao

    companion object {
        @Volatile
        private var INSTANCE: CelesticDatabase? = null

        fun getDatabase(context: Context): CelesticDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CelesticDatabase::class.java,
                    "celestic_database"
                ).fallbackToDestructiveMigration(false).build()
                INSTANCE = instance
                instance
            }
        }
    }
}