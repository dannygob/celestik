package com.example.celestik.models

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a single inspection session.
 * Serves as the parent entity for associated detection items.
 */
@Entity(tableName = "inspections")
data class Inspection(

    /**
     * Auto-generated primary key for Room.
     */
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    /**
     * Timestamp of the inspection in epoch milliseconds.
     */
    val timestamp: Long
)
