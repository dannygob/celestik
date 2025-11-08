package com.example.celestik.navigation

/**
 * Defines all navigation routes used in the Celestik app.
 * Each route is represented as a sealed object with a unique path.
 * Supports dynamic parameters and route generation.
 */
sealed class NavigationRoutes(val route: String) {

    // 🔐 Authentication
    object Login : NavigationRoutes("login")

    // 🏠 Main dashboard
    object Dashboard : NavigationRoutes("dashboard")

    // 📷 Camera and live detection
    object Camera : NavigationRoutes("camera")

    // 📊 Detection details with dynamic type
    object Details : NavigationRoutes("details/{detailType}") {
        fun createRoute(detailType: String) = "details/$detailType"
    }

    // 🎯 Calibration workflow
    object Calibration : NavigationRoutes("calibration")

    // 📄 Report generation dialog
    object ReportDialog : NavigationRoutes("report_dialog")

    // 🔍 Inspection preview screen
    object Preview : NavigationRoutes("inspection_preview")

    // ⚙️ App settings
    object Settings : NavigationRoutes("settings")

    // 📋 List of detected items
    object DetectionList : NavigationRoutes("detection_list")

    // 📌 Inspection status summary
    object Status : NavigationRoutes("status")
}
