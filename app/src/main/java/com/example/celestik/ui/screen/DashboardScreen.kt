package com.example.celestik.ui.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.celestik.R
import com.example.celestik.models.DetectionItem
import com.example.celestik.navigation.NavigationRoutes
import com.example.celestik.utils.LocalizedStringsProvider
import com.example.celestik.utils.ReportGenerator.exportJsonSummary
import com.example.celestik.utils.ReportGenerator.generateCsvFromDetections
import com.example.celestik.utils.ReportGenerator.generatePdfFromDetections
import com.example.celestik.utils.ReportGenerator.generateWordFromDetections
import com.example.celestik.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: MainViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
    val context = LocalContext.current
    val strings = LocalizedStringsProvider.current

    // Estados de los menús
    var menuCalibracion by remember { mutableStateOf(false) }
    var menuConfiguracion by remember { mutableStateOf(false) }
    var menuInspeccion by remember { mutableStateOf(false) }

    // Estados internos
    var useCharuco by remember { mutableStateOf(true) }
    var unidadesSI by remember { mutableStateOf(true) }
    var modoOscuro by remember { mutableStateOf(false) }
    var modoInspeccion by remember { mutableStateOf("Carrocería") }
    var submenuReportes by remember { mutableStateOf(false) }

    val formatos = listOf("PDF", "Word", "JSON", "CSV")
    var formatoSeleccionado by remember { mutableStateOf("PDF") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Celestic Dashboard") },
                actions = {
                    // --- MENU CALIBRACIÓN ---
                    Box {
                        TextButton(onClick = { menuCalibracion = true }) {
                            Text("Calibración")
                        }
                        DropdownMenu(
                            expanded = menuCalibracion,
                            onDismissRequest = { menuCalibracion = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Datos de cámara") },
                                onClick = {
                                    Toast.makeText(context, "Datos de cámara", Toast.LENGTH_SHORT)
                                        .show()
                                    menuCalibracion = false
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("Uso de Charuco")
                                        Spacer(Modifier.width(8.dp))
                                        Switch(
                                            checked = useCharuco,
                                            onCheckedChange = {
                                                useCharuco = it
                                                Toast.makeText(
                                                    context,
                                                    "Charuco: $it",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        )
                                    }
                                },
                                onClick = {}
                            )
                            DropdownMenuItem(
                                text = { Text("Abrir calibración") },
                                onClick = {
                                    Toast.makeText(
                                        context,
                                        strings.toastOpenCalibration,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    navController.navigate(NavigationRoutes.Calibration.route)
                                    menuCalibracion = false
                                }
                            )
                        }
                    }

                    // --- MENU CONFIGURACIÓN ---
                    Box {
                        TextButton(onClick = { menuConfiguracion = true }) {
                            Text("Configuración")
                        }
                        DropdownMenu(
                            expanded = menuConfiguracion,
                            onDismissRequest = { menuConfiguracion = false }
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("Unidades (SI / Imperial)")
                                        Spacer(Modifier.width(8.dp))
                                        Switch(
                                            checked = unidadesSI,
                                            onCheckedChange = {
                                                unidadesSI = it
                                                Toast.makeText(
                                                    context,
                                                    "Unidades: ${if (it) "SI" else "Imperial"}",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        )
                                    }
                                },
                                onClick = {}
                            )
                            DropdownMenuItem(
                                text = { Text("Idioma") },
                                onClick = {
                                    Toast.makeText(
                                        context,
                                        "Selector de idioma",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    menuConfiguracion = false
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("Modo oscuro")
                                        Spacer(Modifier.width(8.dp))
                                        Switch(
                                            checked = modoOscuro,
                                            onCheckedChange = {
                                                modoOscuro = it
                                                Toast.makeText(
                                                    context,
                                                    "Modo oscuro: $it",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        )
                                    }
                                },
                                onClick = {}
                            )
                        }
                    }

                    // --- MENU INSPECCIÓN ---
                    Box {
                        TextButton(onClick = { menuInspeccion = true }) {
                            Text("Inspección")
                        }
                        DropdownMenu(
                            expanded = menuInspeccion,
                            onDismissRequest = { menuInspeccion = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Modo de inspección") },
                                onClick = {}
                            )
                            DropdownMenuItem(
                                text = {
                                    Row {
                                        RadioButton(
                                            selected = modoInspeccion == "Carrocería",
                                            onClick = {
                                                modoInspeccion = "Carrocería"
                                                Toast.makeText(
                                                    context,
                                                    "Modo Carrocería",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        )
                                        Text("Carrocería")
                                    }
                                },
                                onClick = {}
                            )
                            DropdownMenuItem(
                                text = {
                                    Row {
                                        RadioButton(
                                            selected = modoInspeccion == "Lámina",
                                            onClick = {
                                                modoInspeccion = "Lámina"
                                                Toast.makeText(
                                                    context,
                                                    "Modo Lámina",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        )
                                        Text("Lámina")
                                    }
                                },
                                onClick = {}
                            )

                            if (modoInspeccion == "Lámina") {
                                DropdownMenuItem(
                                    text = { Text("Accionar cámara") },
                                    onClick = {
                                        Toast.makeText(
                                            context,
                                            "Cámara activada",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Ejecutar acción") },
                                    onClick = {
                                        Toast.makeText(
                                            context,
                                            "Acción ejecutada",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Generar reporte") },
                                    onClick = { submenuReportes = true }
                                )

                                if (submenuReportes) {
                                    formatos.forEach { formato ->
                                        DropdownMenuItem(
                                            text = { Text(formato) },
                                            onClick = {
                                                formatoSeleccionado = formato
                                                val loteId = "Lote123"
                                                val detecciones = viewModel.detections.value

                                                @Suppress("CAST_NEVER_SUCCEEDS")
                                                when (formato) {
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
                                                }

                                                Toast.makeText(
                                                    context,
                                                    "Reporte $formato generado",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                submenuReportes = false
                                            }
                                        )
                                    }
                                }

                                DropdownMenuItem(
                                    text = { Text("Ver historial") },
                                    onClick = {
                                        Toast.makeText(
                                            context,
                                            "Abriendo historial",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        navController.navigate("detection_list")
                                    }
                                )
                            }
                        }
                    }
                }
            )
        }
    ) { paddingValues ->

        // --- ÁREA PRINCIPAL: CÁMARA ---
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            // Placeholder de cámara
            Image(
                painter = painterResource(id = R.drawable.placeholder_camera),
                contentDescription = "Vista previa de cámara",
                modifier = Modifier.fillMaxSize(0.9f)
            )

            // Fecha, hora y precisión
            val fecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            val hora = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Text(text = fecha, color = Color.White)
                Text(text = hora, color = Color.White)
                Text(text = "Precisión: --", color = Color.White)
            }
        }
    }
}