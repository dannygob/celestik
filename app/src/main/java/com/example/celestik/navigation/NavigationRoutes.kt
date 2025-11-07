package com.example.celestik.navigation

sealed class NavigationRoutes(val route: String) {

    object Login : NavigationRoutes("login")

    object Dashboard : NavigationRoutes("dashboard")

    object Camera : NavigationRoutes("camera")

    object Details : NavigationRoutes("details/{detailType}") {
        fun createRoute(detailType: String) = "details/$detailType"
    }

    object Calibration : NavigationRoutes("calibration")

    object ReportDialog : NavigationRoutes("report_dialog")

    object Preview : NavigationRoutes("inspection_preview")

    object Settings : NavigationRoutes("settings")

    object DetectionList : NavigationRoutes("detection_list")

    object Status : NavigationRoutes("status")
}
