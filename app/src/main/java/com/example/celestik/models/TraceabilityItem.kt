package com.example.celestik.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TraceabilityItem(
    val codigo: String,
    val pieza: String,
    val operario: String,
    val fecha: String,
    val resultado: String,
) : Parcelable