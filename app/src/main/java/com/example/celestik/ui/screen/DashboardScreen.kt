/**
 * Displays the main dashboard screen with navigation, calibration toggle,
 * inspection modes, and report export options.
 *
 * @param navController Navigation controller for routing.
 * @param viewModel ViewModel containing detection data and app state.
 */
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

            // Calibration toggle
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

            // Inspection modes
            Card(elevation = CardDefaults.cardElevation()) {
                Column(Modifier.padding(16.dp)) {
                    Text(strings.analysisModes, style = MaterialTheme.typography.titleMedium)

                    Row {
                        Button(onClick = {
                            Toast.makeText(context, strings.toastModeBody, Toast.LENGTH_SHORT).show()
                        }) {
                            Text(strings.modeBody)
                        }

                        Spacer(Modifier.width(8.dp))

                        Button(onClick = {
                            Toast.makeText(context, strings.toastModePrecision, Toast.LENGTH_SHORT).show()
                        }) {
                            Text(strings.modePrecision)
                        }

                        Spacer(Modifier.width(8.dp))

                        Button(onClick = {
                            Toast.makeText(context, strings.toastModeMetals, Toast.LENGTH_SHORT).show()
                        }) {
                            Text(strings.modeMetals)
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // Report generation
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
                            "PDF" -> generatePdfFromDetections(context, detecciones as List<DetectionItem>, loteId)
                            "Word" -> generateWordFromDetections(context, detecciones as List<DetectionItem>, loteId)
                            "JSON" -> exportJsonSummary(context, detecciones as List<DetectionItem>, loteId)
                            "CSV" -> generateCsvFromDetections(context, detecciones as List<DetectionItem>, loteId)
                            else -> null
                        }

                        archivo?.let {
                            Toast.makeText(
                                context,
                                "Reporte generado: ${it.name}",
                                Toast.LENGTH_LONG
                            ).show()
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

            // Language hint
            Text(
                text = strings.languageSettingHint,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
