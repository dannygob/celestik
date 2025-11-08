package com.example.celestik.navigation

import DashboardScreen
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
import com.example.celestik.ui.screen.DetailsScreen
import com.example.celestik.ui.screen.DetectionListScreen
import com.example.celestik.ui.screen.InspectionPreviewScreen
import com.example.celestik.ui.screen.LoginScreen
import com.example.celestik.ui.screen.ReportRequestDialog
import com.example.celestik.ui.screen.SettingsScreen
import com.example.celestik.ui.screen.StatusScreen

/**
 * Defines the navigation graph for the Celestik app.
 * Maps route constants to screen composables and injects dependencies as needed.
 */
@Composable
fun NavigationGraph(navController: NavHostController) {
    // Instance of AprilTag detector used in CameraScreen
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
                onConfirm = { navController.popBackStack() } // Replace with actual submission logic if needed
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

        composable(NavigationRoutes.Status.route) {
            StatusScreen(navController)
        }
    }
}
