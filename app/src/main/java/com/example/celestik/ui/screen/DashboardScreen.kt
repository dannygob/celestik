package com.example.celestik.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
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
    val formatos = listOf("PDF", "Word", "JSON", "CSV")
    var formatoSeleccionado by remember { mutableStateOf("PDF") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(strings.appBarTitle) })
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

            // 🔧 Calibración
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
                            ).show()
                            navController.navigate(NavigationRoutes.Calibration.route)
                        }) {
                            Text(strings.openCalibration)
                        }

                        Spacer(Modifier.width(16.dp))

                        Switch(
                            checked = useCharuco,
                            onCheckedChange = {
                                useCharuco = it
                                val marker = if (useCharuco) "Charuco" else "AprilTag"
                                Toast.makeText(
                                    context,
                                    "Marcador seleccionado: $marker",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )

                        Spacer(Modifier.width(8.dp))
                        Text(if (useCharuco) "Charuco" else "AprilTag")
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // 🚀 Modos de inspección
            Card(elevation = CardDefaults.cardElevation()) {
                Column(Modifier.padding(16.dp)) {
                    Text(strings.analysisModes, style = MaterialTheme.typography.titleMedium)

                    Row {
                        Button(onClick = {
                            Toast.makeText(context, strings.toastModeCarroceria, Toast.LENGTH_SHORT)
                                .show()
                        }) {
                            Text(strings.modeCarroceria)
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
                            Toast.makeText(context, strings.toastModeMetales, Toast.LENGTH_SHORT)
                                .show()
                        }) {
                            Text(strings.modeMetales)
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // 📊 Reportes
            Card(elevation = CardDefaults.cardElevation()) {
                Column(Modifier.padding(16.dp)) {
                    Text(strings.reportsSection, style = MaterialTheme.typography.titleMedium)

                    Spacer(Modifier.height(8.dp))

                    Text(strings.exportFormat, style = MaterialTheme.typography.bodyMedium)

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        formatos.forEach { formato ->
                            val isSelected = formatoSeleccionado == formato
                            Button(
                                onClick = { formatoSeleccionado = formato },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                                    contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                                )
                            ) {
                                Text(formato)
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Button(onClick = {
                        val loteId = "Lote123" // 🔧 Puedes hacerlo dinámico si quieres
                        val detecciones = viewModel.detections.value

                        val archivo = when (formatoSeleccionado) {
                            "PDF" -> generatePdfFromDetections(context, detecciones, loteId)
                            "Word" -> generateWordFromDetections(context, detecciones, loteId)
                            "JSON" -> exportJsonSummary(context, detecciones, loteId)
                            "CSV" -> generateCsvFromDetections(context, detecciones, loteId)
                            else -> null
                        }

                        if (archivo != null) {
                            Toast.makeText(
                                context,
                                "${strings.reportGenerated}: ${archivo.name}",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                context,
                                strings.reportError,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }) {
                        Text("${strings.generateReportButton} ($formatoSeleccionado)")
                    }

                    Spacer(Modifier.height(8.dp))

                    Button(onClick = {
                        navController.navigate(NavigationRoutes.DetectionList.route)
                    }) {
                        Text(strings.viewDetections)
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // 🌐 Idioma
            Text(
                text = strings.languageSettingHint,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
