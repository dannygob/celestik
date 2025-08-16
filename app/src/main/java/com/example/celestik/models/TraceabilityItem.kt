package com.example.celestik.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TraceabilityItem(
    val code: String,
    val Pieces: String,
    val operator: String,
    val Date: String,
    val results: String,
) : Parcelable