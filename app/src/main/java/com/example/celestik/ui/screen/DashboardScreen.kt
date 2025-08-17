package com.example.celestik.ui.screen


import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.celestik.models.DetectionItem
import com.example.celestik.navigation.NavigationRoutes
import com.example.celestik.utils.LocalizedStrings
import com.example.celestik.utils.exportJsonSummary
import com.example.celestik.utils.generateCsvFromDetections
import com.example.celestik.utils.generatePdfFromDetections
import com.example.celestik.utils.generateWordFromDetections
import com.example.celestik.viewmodel.MainViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: MainViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
    val context = LocalContext.current
    val strings = LocalizedStrings.current
    var useCharuco by remember { mutableStateOf(true) }
    val formats = listOf("PDF", "Word", "JSON", "CSV")
    var formatSelected by remember { mutableStateOf("PDF") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Celestic Dashboard") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
        ) {
            Text(
                text = strings.dashboardTitle,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.height(16.dp))

            // 🔧 Settings Section
            Card(elevation = CardDefaults.cardElevation()) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        text = strings.calibrationSection,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Button(onClick = {
                            Toast.makeText(
                                context,
                                strings.toastOpenCalibration,
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            navController.navigate(NavigationRoutes.Calibration.route)
                        }) {
                            Text(strings.openCalibration)
                        }

                        Spacer(Modifier.width(16.dp))

                        Switch(checked = useCharuco, onCheckedChange = {
                            useCharuco = it
                            val marker = if (useCharuco) "Charuco" else "AprilTag"
                            Toast.makeText(
                                context,
                                "Marcador seleccionado: $marker",
                                Toast.LENGTH_SHORT
                            ).show()
                        })
                        Text(if (useCharuco) "Charuco" else "AprilTag")
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // 🚀 Inspection Modes Section
            Card(elevation = CardDefaults.cardElevation()) {
                Column(Modifier.padding(16.dp)) {
                    Text(strings.analysisModes, style = MaterialTheme.typography.titleMedium)

                    Row {
                        Button(onClick = {
                            Toast.makeText(context, strings.toastModeBody, Toast.LENGTH_SHORT)
                                .show()
                        }) {
                            Text(strings.modeBody)
                        }

                        Spacer(Modifier.width(8.dp))

                        Button(onClick = {
                            Toast.makeText(context, strings.toastModePrecision, Toast.LENGTH_SHORT)
                                .show()
                        }) {
                            Text(strings.modePrecision)
                        }

                        Spacer(Modifier.width(8.dp))

                        Button(onClick = {
                            Toast.makeText(context, strings.toastModeMetals, Toast.LENGTH_SHORT)
                                .show()
                        }) {
                            Text(strings.modeMetals)
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // 📊 History and Report Generation Section
            Card(elevation = CardDefaults.cardElevation()) {
                Column(Modifier.padding(16.dp)) {
                    Text(strings.reportsSection, style = MaterialTheme.typography.titleMedium)

                    Spacer(Modifier.height(8.dp))

                    Text("Formato de exportación:", style = MaterialTheme.typography.bodyMedium)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        formats.forEach { formato ->
                            Button(onClick = { formatSelected = formato }) {
                                Text(formato)
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Button(onClick = {
                        val loteId = "Lote123"
                        val detecciones = viewModel.detections.value

                        val archivo = when (formatSelected) {
                            "PDF" -> generatePdfFromDetections(
                                context,
                                detecciones as List<DetectionItem>, loteId
                            )

                            "Word" -> generateWordFromDetections(
                                context,
                                detecciones as List<DetectionItem>, loteId
                            )

                            "JSON" -> exportJsonSummary(
                                context,
                                detecciones as List<DetectionItem>, loteId
                            )

                            "CSV" -> generateCsvFromDetections(
                                context,
                                detecciones as List<DetectionItem>, loteId
                            )

                            else -> null
                        }

                        archivo?.let {
                            Toast.makeText(
                                context,
                                "Reporte generado: ${it.name}",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }
                    }) {
                        Text("Generar reporte ($formatSelected)")
                    }

                    Spacer(Modifier.height(8.dp))

                    Button(onClick = {
                        navController.navigate("detection_list")
                    }) {
                        Text("View Detections")
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // 🌐 Multilenguaje
            Text(
                text = strings.languageSettingHint,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
