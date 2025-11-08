package com.example.celestik.utils

/**
 * Contains hardcoded Spanish UI strings for the Celestik app.
 * Intended for future internationalization via CompositionLocal or dynamic loading.
 */
data class LocalizedStrings(
    val dashboardTitle: String = "Panel principal",
    val calibrationSection: String = "Calibración y configuración",
    val openCalibration: String = "Abrir calibración",
    val analysisModes: String = "Modos de inspección",
    val modeBody: String = "Carrocería",
    val modePrecision: String = "Precisión",
    val modeMetals: String = "Metal",
    val reportsSection: String = "Reportes y historial",
    val generateReport: String = "Generar reporte",
    val viewHistory: String = "Ver historial",
    val languageSettingHint: String = "Idioma actual: Español",
    val toastOpenCalibration: String = "Navegando a calibración...",
    val toastOpenDetails: String = "Abriendo historial...",
    val toastOpenReportDialog: String = "Solicitando generación de reporte...",
    val toastModeBody: String = "Mod Carrocería seleccionado",
    val toastModePrecision: String = "Modo Precisión seleccionado",
    val toastModeMetals: String = "Modo Metal seleccionado",
    val loginEmailLabel: String = "Email",
    val loginPasswordLabel: String = "Contraseña",
    val loginButtonLabel: String = "Iniciar sesión",
    val loginValidationMessage: String = "Por favor ingresa email y contraseña.",
    val loginErrorMessage: String = "Error al iniciar sesión.",

    val settingsTitle: String = "Configuración",
    val settingsDarkThemeLabel: String = "Tema oscuro",
    val settingsUnitLabel: String = "Usar pulgadas",
    val settingsMarkerLabel: String = "Marcador de detección",

    val statusScreenTitle: String = "Pantalla de estado",
    val inspectionPreviewTitle: String = "Visualización previa",
    val inspectionPreviewButton: String = "Ver componentes",

    val detectionDetailsLabel: String = "Detalles para la detección: %s",
    val detectionListErrorMessage: String = "❌ Error al cargar las detecciones.",
    val confirmButtonLabel: String = "Aceptar",
    val logOpenCVSuccess: String = "OpenCV initialized successfully",
    val logOpenCVFailure: String = "OpenCV initialization failed",
    val reportTitle: String = "Reporte de Detecciones - Lote: %s",
    val reportFieldId: String = "ID: %s",
    val reportFieldType: String = "Tipo: %s",
    val reportFieldConfidence: String = "Confianza: %s",
    val reportFieldStatus: String = "Status: %s",
    val reportSeparator: String = "--------------------",
    val reportCsvHeader: String = "ID,Tipo,Confianza,Status,Medida (mm)",

    )

// TODO: Enable CompositionLocal for dynamic localization support
// val LocalizedStrings = compositionLocalOf { LocalizedStrings() }
