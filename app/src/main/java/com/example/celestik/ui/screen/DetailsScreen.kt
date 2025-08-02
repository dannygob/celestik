package com.example.celestik.ui.screen

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.celestik.models.DetectionItem
import com.example.celestik.ui.component.BlueprintView
import com.example.celestik.utils.Result
import com.example.celestik.viewmodel.DetailsViewModel
import com.example.celestik.viewmodel.SharedViewModel

@Composable
fun DetailsScreen(
    navController: NavController,
    detailType: String,
    detectionItem: DetectionItem? = null,
    detailsViewModel: DetailsViewModel = viewModel(),
    sharedViewModel: SharedViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val title = when (detailType) {
        "hole" -> stringResource(R.string.details_title_hole)
        "alodine" -> stringResource(R.string.details_title_alodine)
        "countersink" -> stringResource(R.string.details_title_countersink)
        else -> "Detalle"
    }

    LaunchedEffect(detectionItem) {
        detectionItem?.linkedQrCode?.let { codigo ->
            viewModel.loadTrazabilidad(codigo)
        }
        detectionItem?.id?.let {
            viewModel.loadFeatures(it)
        }
    }

    val trazabilidadResult by detailsViewModel.trazabilidadItem.collectAsState()
    val features by detailsViewModel.features.collectAsState()
    val useInches by sharedViewModel.useInches.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = title, style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        BlueprintView(features = features, useInches = useInches)

        Spacer(modifier = Modifier.height(16.dp))

        features.forEach { feature ->
            Text("Feature: ${feature.featureType} at (${feature.xCoord}, ${feature.yCoord})")
        }

        Spacer(modifier = Modifier.height(16.dp))

        AnimatedVisibility(
            visible = trazabilidadResult is Result.Success,
            enter = fadeIn(animationSpec = tween(durationMillis = 1000))
        ) {
            val trazabilidad =
                (trazabilidadResult as Result.Success).data
            trazabilidad?.let {
                Divider()
                Text("🔍 Trazabilidad:", style = MaterialTheme.typography.titleMedium)
                Text("• Código: ${it.codigo}")
                Text("• Pieza: ${it.pieza}")
                Text("• Operario: ${it.operario}")
                Text("• Fecha: ${it.fecha}")
                Text("• Resultado: ${it.resultado}")
            } ?: Text("❌ No hay información de trazabilidad.")
        }

        if (trazabilidadResult is Result.Loading) {
            CircularProgressIndicator()
        }

        if (trazabilidadResult is Result.Error) {
            Text("❌ Error al cargar la información de trazabilidad.")
        }

        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = {
            Toast.makeText(context, stringResource(R.string.report_issue), Toast.LENGTH_SHORT)
                .show()
        }) {
            Text(stringResource(R.string.report_issue))
        }
    }
}