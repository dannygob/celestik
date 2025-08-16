package com.example.celestik.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.celestik.manager.AprilTagManager
import com.example.celestik.ui.screen.CalibrationScreen
import com.example.celestik.ui.screen.CameraScreen
import com.example.celestik.ui.screen.DashboardScreen
import com.example.celestik.ui.screen.DetailsScreen
import com.example.celestik.ui.screen.DetectionListScreen
import com.example.celestik.ui.screen.InspectionPreviewScreen
import com.example.celestik.ui.screen.LoginScreen
import com.example.celestik.ui.screen.ReportRequestDialog
import com.example.celestik.ui.screen.SettingsScreen

@Composable
fun NavigationGraph(navController: NavHostController) {
    // Crear y recordar el AprilTagManager para detección de tags
    val aprilTagManager = remember { AprilTagManager().apply { init() } }

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(navController)
        }

        composable(NavigationRoutes.Dashboard.route) {
            DashboardScreen(navController)
        }

        composable(NavigationRoutes.Camera.route) {
            // Pasamos la instancia del detector al CameraScreen
            CameraScreen(navController, aprilTagManager)
        }

        composable(
            NavigationRoutes.Details.route,
            arguments = listOf(navArgument("detailType") { type = NavType.StringType })
        ) { backStackEntry ->
            val detailType = backStackEntry.arguments?.getString("detailType") ?: "hole"
            DetailsScreen(navController, detailType)
        }

        composable(NavigationRoutes.Calibration.route) {
            CalibrationScreen(navController)
        }

        composable(NavigationRoutes.ReportDialog.route) {
            ReportRequestDialog(
                onDismiss = { navController.popBackStack() },
                onConfirm = { navController.popBackStack() } // lógica real si quieres enviar algo
            )
        }

        composable(NavigationRoutes.Preview.route) {
            InspectionPreviewScreen(navController)
        }

        composable("settings") {
            SettingsScreen(navController)
        }

        composable("detection_list") {
            DetectionListScreen(navController)
        }
    }
}
