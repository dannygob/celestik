package com.example.celestik.database.converters

import androidx.room.TypeConverter
import com.example.celestik.models.enums.DetectionStatus
import com.example.celestik.models.enums.DetectionType
import com.example.celestik.models.geometry.BoundingBox
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromBoundingBox(value: BoundingBox): String = gson.toJson(value)

    @TypeConverter
    fun toBoundingBox(value: String): BoundingBox = gson.fromJson(value, BoundingBox::class.java)

    @TypeConverter
    fun fromMap(value: Map<String, Float>): String = gson.toJson(value)

    @TypeConverter
    fun toMap(value: String): Map<String, Float> {
        val type = object : TypeToken<Map<String, Float>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromDetectionStatus(status: DetectionStatus): String = status.name

    @TypeConverter
    fun toDetectionStatus(value: String): DetectionStatus = DetectionStatus.valueOf(value)

    @TypeConverter
    fun fromDetectionType(type: DetectionType): String = type.name

    @TypeConverter
    fun toDetectionType(value: String): DetectionType = DetectionType.valueOf(value)
}
