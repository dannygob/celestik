package com.example.celestik.utils

import android.content.Context
import com.example.celestik.models.TraceabilityItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


fun loadTractabilityFromJson(context: Context): List<TraceabilityItem> {
    val json = context.assets.open("tractability.json").bufferedReader().use { it.readText() }
    val type = object : TypeToken<List<TraceabilityItem>>() {}.type
    return Gson().fromJson(json, type)
}

fun searchForCode(code: String, list: List<TraceabilityItem>): TraceabilityItem? {
    return list.firstOrNull { it.codigo == code }
}