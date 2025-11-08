package com.example.celestik.utils

import android.content.Context
import com.example.celestik.models.TraceabilityItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Loads traceability data from a bundled JSON asset.
 *
 * @param context Application context.
 * @return List of TraceabilityItem parsed from JSON.
 */
fun loadTraceabilityFromJson(context: Context): List<TraceabilityItem> {
    val json = context.assets.open("traceability.json").bufferedReader().use { it.readText() }
    val type = object : TypeToken<List<TraceabilityItem>>() {}.type
    return Gson().fromJson(json, type)
}

/**
 * Searches for a traceability item by code.
 *
 * @param code Traceability code to search for.
 * @param list List of TraceabilityItem to search within.
 * @return Matching TraceabilityItem or null if not found.
 */
fun searchForCode(code: String, list: List<TraceabilityItem>): TraceabilityItem? {
    return list.firstOrNull { it.code == code }
}
