package com.example.celestik.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.celestik.manager.AprilTagManager
import com.example.celestik.ui.screen.*

@Composable
fun NavigationGraph(navController: NavHostController) {
    // Instancia del detector de AprilTags
    val aprilTagManager = remember { AprilTagManager().apply { init() } }

    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.Login.route
    ) {
        composable(NavigationRoutes.Login.route) {
            LoginScreen(navController)
        }

        composable(NavigationRoutes.Dashboard.route) {
            DashboardScreen(navController)
        }

        composable(NavigationRoutes.Camera.route) {
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
                onConfirm = { navController.popBackStack() } // lógica real si se desea enviar algo
            )
        }

        composable(NavigationRoutes.Preview.route) {
            InspectionPreviewScreen(navController)
        }

        composable(NavigationRoutes.Settings.route) {
            SettingsScreen(navController)
        }

        composable(NavigationRoutes.DetectionList.route) {
            DetectionListScreen(navController)
        }

        composable("status") {
            StatusScreen(navController)
        }
    }
}
