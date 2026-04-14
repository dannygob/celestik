# Project Analysis

## `app/src/main/java/com/example/celestik`

### `assets`

- `mobilenet_v2.tflite`: A TensorFlow Lite model, likely for image classification.
- `trazabilidad.json`: A JSON file, likely containing traceability data.

### `camera`

- `CameraFrameProcessor.kt`: Processes camera frames using OpenCV and a `FrameAnalyzer`.
- `CameraXManager.kt`: Manages the CameraX lifecycle, including setup, preview, and image analysis.

### `config`

- `config_report.json`: A JSON file containing configuration for generating reports.

### `data`

- `dao/CelestikDao.kt`: A Room DAO that defines all database operations for the application.
- `repository/DetectionRepository.kt`: A repository that provides an abstraction layer over the `CelestikDao`.

### `database`

- `converters/Converters.kt`: A file containing Room `TypeConverter`s for custom data types.
- `CelestikDatabase.kt`: Defines the Room database configuration, registers entities, and provides access to the DAO.

### `di`

- `DatabaseModule.kt`: A Hilt module that provides instances of the Room database and DAO.
- `RepositoryModule.kt`: A Hilt module that provides an instance of the `DetectionRepository`.

### `manager`

- `AprilTagManager.kt`: Manages the detection of AprilTag markers and the generation of virtual tags.
- `ArUcoManager.kt`: Handles the detection of ArUco markers using OpenCV.
- `CalibrationManager.kt`: Manages camera calibration using Charuco boards and OpenCV.
- `ImageClassifier.kt`: Loads a TFLite model and performs inference on input Bitmaps.

### `models`

#### `calibration`

- `CameraCalibrationData.kt`: A Room entity that represents camera calibration data.
- `DetectedFeature.kt`: A Room entity that represents a detected visual feature.
- `calibration.json`: A JSON file containing camera calibration data. **Note:** This file may be redundant, as `CalibrationManager` loads a similar file from the application's internal storage.

#### `enums`

- `DetectionStatus.kt`: An enum that represents the status of a detection.
- `DetectionType.kt`: An enum that represents the type of feature or anomaly detected.
- `MeasurementUnit.kt`: An enum that defines the possible units of measurement.
- `ReportFormat.kt`: An enum that defines the possible formats for reports.

#### `geometry`

- `BoundingBox.kt`: A data class that represents the bounding area of a detected feature.

#### `report`

- `ReportConfig.kt`: A Room entity that defines the configuration for report export.
- `ReportEntry.kt`: A Room entity that represents a single entry in a report.

- `DetectionItem.kt`: A Room entity that represents a single detection result.
- `DetectionItemWithTraceability.kt`: A data class that combines a `DetectionItem` with optional traceability metadata.
- `Inspection.kt`: A Room entity that represents a single inspection session.
- `MeasurementSet.kt`: A data class that represents a set of technical measurements.
- `TraceabilityItem.kt`: A data class that represents traceability metadata. **Note:** This file contains a typo (`Date` should be `date`) and a duplicated field (`Pieces`).

### `navigation`

- `NavRoutes.kt`: An object that defines navigation route constants. **Note:** This file is redundant and should be merged with `NavigationRoutes.kt`.
- `NavigationGraph.kt`: Defines the navigation graph for the application.
- `NavigationRoutes.kt`: A sealed class that defines all navigation routes.

### `opencv`

- `FrameAnalyzer.kt`: Performs various image analysis tasks using OpenCV, such as contour detection, watershed segmentation, and optical flow.
- `ImageProcessor.kt`: An object that appears to be a placeholder for image processing logic. **Note:** This file seems to be an orphan file, as it is not used anywhere in the codebase.
- `OpenCVInitializer.kt`: An object that initializes the OpenCV library.

### `trace`

- `tazabilidad.json`: A JSON file containing traceability data. **Note:** This file is a duplicate of `trazabilidad.json` in the `assets` directory and contains a typo in its name. It should be removed.

### `ui`

#### `component`

- `BlueprintView.kt`: A composable that displays a blueprint-style view with markers and detected features.
- `DetectionItemCard.kt`: A composable that displays a summary card for a `DetectionItem`. **Note:** This file has a compilation error, as it references a non-existent `measurementMm` field in the `DetectionItem` data class.
- `DrawingCanvas.kt`: A composable that renders a canvas with detected features.
- `FeatureCard.kt`: A composable that displays a clickable card with a title and description.
- `MeasurementOverlay.kt`: A composable that displays a measurement label in the top-left corner of the screen.
- `ShimmerDetectionItemCard.kt`: A composable that displays a shimmer placeholder card. **Note:** This component uses the `facebook.shimmer` library, which is not listed as a dependency.
- `StatusIndicator.kt`: A composable that displays a circular status indicator.

#### `preview`

- `DetectionItemPreview.kt`: A composable that displays a simple text preview of a `DetectionItem`. **Note:** This file has the same compilation error as `DetectionItemCard.kt`, as it references a non-existent `measurementMm` field in the `DetectionItem` data class.

#### `scanner`

- `startQrScan.kt`: An object that provides QR code scanning utilities using OpenCV.

#### `screen`

- `CalibrationScreen.kt`: A composable that displays the calibration screen. **Note:** It references a non-existent string resource `R.string.openCalibration`.
- `CameraScreen.kt`: A composable that displays a live camera feed and analyzes frames. **Note:** The `aprilTagManager` parameter is unused and marked with a TODO to be removed.
- `CameraView.kt`: A composable that displays a camera preview and performs real-time image classification. **Note:** This file has several issues: it incorrectly references `Manifest.permission.CAMERA`, calls a non-existent `initOpenCV` function in `OpenCVInitializer`, and the `startCamera` function is also missing. It is also redundant with `CameraScreen.kt`.
- `DashboardScreen.kt`: A composable that displays the main dashboard. **Note:** It references a non-existent `LocalizedStrings` object and a `ReportGenerator` object.
- `DetailsScreen.kt`: A composable that displays detailed information about a detection item. **Note:** This file has several issues: it references an undefined `Result` class and several undefined string resources, it calls `BlueprintView` with incorrect parameters, it uses the `TraceabilityItem` with the typo and duplicated field, and it has an incorrect import for `Date`.
- `DetectionDetailsScreen.kt`: A composable that is a placeholder for displaying detection details. **Note:** This screen is redundant, as `DetailsScreen.kt` already provides this functionality.
- `DetectionListScreen.kt`: A composable that displays a scrollable list of detection items. **Note:** It uses the undefined `Result` class and has a generic type warning.
- `InspectionPreviewScreen.kt`: A placeholder composable for an inspection preview screen.
- `LoginScreen.kt`: A composable that displays a login form and authenticates the user. **Note:** It uses `FirebaseAuth`, but the Firebase dependency is not declared in the build scripts. The navigation route is also hardcoded.
- `ReportRequestDialog.kt`: A composable that displays a confirmation dialog for report generation. **Note:** It references several non-existent string resources.
- `SettingsScreen.kt`: A composable that displays the settings screen. **Note:** It references a `MarkerType` enum in the `viewmodel` package, which is not the correct location for this type of file.
- `StatusScreen.kt`: A placeholder composable for a status screen.

#### `theme`

- `CelesticTheme.kt`: A composable that applies the Celestic theme to the app.
- `Color.kt`: A file that defines several colors. **Note:** These colors are not used anywhere in the app, so this file is an orphan.
- `Shape.kt`: A file that defines the shape configuration for the app's Material 3 theme.
- `Theme.kt`: A composable that applies the Celestik theme to the app. **Note:** This file is redundant with `CelesticTheme.kt` and should be removed.
- `Typography.kt`: A file that defines the base typography styles for the app.

### `utils`

- `CameraUtils.kt`: A file containing utility functions for CameraX.
- `ImageUtils.kt`: A file containing utility functions for image processing with OpenCV. **Note:** This file contains several functions that are duplicates of functions in `FrameAnalyzer.kt`.
- `LocalizedStrings.kt`: A data class containing all the hardcoded strings. **Note:** These strings should be moved to `strings.xml`.
- `OpenCVInitializer.kt`: An object that initializes the OpenCV library. **Note:** This file is a duplicate of `com.example.celestik.opencv.OpenCVInitializer.kt`.
- `ReportGenerator.kt`: An object that generates reports in different formats. **Note:** This file uses the `itextpdf` and `poi` libraries, which are not listed as dependencies. It also contains a hardcoded directory path.
- `Result.kt`: A sealed class that represents the result of an operation.
- `loadTraceabilityFromJson.kt`: A file containing utility functions for loading traceability data from a JSON file.

### `viewmodel`

- `DetailsViewModel.kt`: A ViewModel for loading traceability data and detected features. **Note:** It has a TODO comment to log an error.
- `MainViewModel.kt`: A ViewModel for exposing the list of detections.
- `SharedViewModel.kt`: A ViewModel that holds UI-related shared state. **Note:** This file contains the `MarkerType` enum, which should be moved to the `models/enums` directory.

- `MainActivity.kt`: The main activity and entry point of the application. **Note:** This file passes a modifier to the `NavigationGraph` composable, which does not accept one.

## 2. Duplicate and Orphaned Files

### Duplicate Files

- `NavRoutes.kt` and `NavigationRoutes.kt`: Both define navigation routes. `NavRoutes.kt` is an object with constants, while `NavigationRoutes.kt` is a sealed class. `NavigationRoutes.kt` is more feature-rich and should be the single source of truth.
- `OpenCVInitializer.kt` in `utils` and `opencv` packages: Both files initialize OpenCV. The one in the `opencv` package is the one being used.
- `Theme.kt` and `CelesticTheme.kt`: Both files define the app's theme. `CelesticTheme.kt` is the one being used.
- `trazabilidad.json` and `tazabilidad.json`: These files are duplicates, and `tazabilidad.json` has a typo in its name.

### Orphaned Files

- `ImageProcessor.kt`: This file is not used anywhere in the codebase.
- `Color.kt`: The colors defined in this file are not used in the app's theme.
- `progress.md`: This file is not referenced anywhere.
- `DetectionDetailsScreen.kt`: This screen is a placeholder and is redundant with `DetailsScreen.kt`.

### Duplicate Functions

- The following functions are duplicated between `FrameAnalyzer.kt` and `ImageUtils.kt`:
    - `generateWatershedMarkers`
    - `detectHoles`
    - `calculateMeasurements`
    - `detectDeformationsWithOpticalFlow` (`trackFeaturesWithOpticalFlow` in `ImageUtils.kt`)
    - `detectEdges` (`detectEdgesCanny` in `ImageUtils.kt`)

## 3. Hardcoded Values

### Hardcoded Strings

The `LocalizedStrings.kt` file contains a large number of hardcoded strings that should be moved to `strings.xml`. Additionally, there are many other hardcoded strings throughout the codebase, including:

- UI text in composables (e.g., "OK", "Details", "Status Screen", "Celestic Dashboard", "Login")
- Log messages
- Error messages
- Navigation routes (e.g., "dashboard", "detection_list")
- JSON keys (e.g., "cameraMatrix", "distortionCoeffs")
- File names and paths (e.g., "/storage/emulated/0/Celestik/Reports/", "ReporteCelestic_")

### Hardcoded Colors

- `StatusIndicator.kt`: The colors for the active and inactive states are hardcoded.
- `CelesticTheme.kt`: The color palettes are defined with hardcoded color values.

### Hardcoded Dimensions

- Numerous composables use hardcoded `dp` values for padding, spacing, and sizing. These should be moved to a `dimens.xml` file or a constants file.

## 4. Overall Architecture

The project attempts to follow a **Model-View-ViewModel (MVVM)** architecture, which is a standard for modern Android development. Hilt is used for dependency injection.

### Architecture Assessment

- **MVVM Structure:** The project is structured into `data` (Model), `ui` (View), and `viewmodel` (ViewModel) packages, which aligns with MVVM.
- **Data Layer:** A Repository pattern (`DetectionRepository`) is used to abstract the data source (`CelestikDao`), which is a good practice.

### Inconsistencies and Principle Violations

- **Separation of Concerns:** The separation between the View and ViewModel is not well-enforced.
    - `LoginScreen.kt` directly calls `FirebaseAuth.getInstance()`. All authentication logic should be handled in the `ViewModel` and abstracted away by a repository.
    - `DashboardScreen.kt` calls the `ReportGenerator` directly. This is business logic that should be invoked from the `ViewModel`.
- **Single Responsibility Principle (SRP):**
    - `FrameAnalyzer.kt` is a large class with too many responsibilities, including various types of image processing, marker detection, and analysis. This class should be broken down into smaller, more focused components.
- **Don't Repeat Yourself (DRY):**
    - There is significant code duplication, as noted in the "Duplicate and Orphaned Files" section. This includes duplicate functions, classes, and even resource files. The logic for converting an `ImageProxy` to a `Mat` is also duplicated in `CameraScreen.kt` and `CameraFrameProcessor.kt`.
- **Model Placement:**
    - The `MarkerType` enum is located in the `viewmodel` package, but it should be in `models/enums` as it's a data model.

## 5. Jetpack Compose Usage

The project uses Jetpack Compose for its UI layer. While it successfully builds screens, there are several areas where best practices are not followed, leading to potential performance issues and poor maintainability.

### State Management

- **State Hoisting:** State is often not hoisted correctly.
    - In `LoginScreen.kt`, the `email`, `password`, `errorMessage`, and `isLoading` states should be hoisted to a `LoginViewModel`.
    - In `DashboardScreen.kt`, the `useCharuco` and `formatSelected` states should be hoisted to the `MainViewModel`.
- **`remember` Usage:** The `remember` function is used incorrectly in `NavigationGraph.kt`, where a new `AprilTagManager` is created on every recomposition. This is inefficient and the `AprilTagManager` should be provided by a ViewModel.

### Performance

- **`@Stable` and `@Immutable`:** None of the data classes in the `models` directory are annotated with `@Stable` or `@Immutable`. This can cause unnecessary recompositions, as Compose cannot be certain that the data is immutable.
- **Inefficient Object Creation:** In `CameraScreen.kt`, a new `FrameAnalyzer` is created on every recomposition. This is highly inefficient and the `FrameAnalyzer` should be created once in the `CameraViewModel`.

### Composable Structure

- **Large Composable Functions:** `DashboardScreen.kt` is a very large composable function that could be broken down into smaller, more manageable pieces.

## 6. ViewModel Implementation

The project uses ViewModels to manage UI state, but there are inconsistencies and areas for improvement.

### ViewModel Usage

- **Injection:** The project is inconsistent in its use of `viewModel()` and `hiltViewModel()`. Since Hilt is used for dependency injection, `hiltViewModel()` should be used consistently to retrieve ViewModels.
- **State Exposure:** The ViewModels correctly use `StateFlow` to expose state to the UI, which is a good practice.

### State Management

- **State in Composables:** As noted in the Jetpack Compose section, a lot of state is managed directly in the composables using `remember { mutableStateOf(...) }`. This state should be hoisted to the ViewModels to make the composables stateless and easier to test.

### Separation of Concerns

- **Logic in the UI:** There are several instances of business logic being placed in the UI layer instead of the ViewModel.
    - `LoginScreen.kt` directly uses `FirebaseAuth`. This logic should be moved to a `LoginViewModel` and abstracted away by a repository.
    - `DashboardScreen.kt` calls the `ReportGenerator` directly. This logic should also be moved to the `MainViewModel`.

## 7. Navigation

The project uses Jetpack Navigation for navigating between composables.

### Navigation Graph

- **Clarity:** The navigation graph is defined in `NavigationGraph.kt` and is relatively easy to follow.
- **Duplicate Routes:** The project has two files that define navigation routes: `NavRoutes.kt` and `NavigationRoutes.kt`. This is a clear violation of the DRY principle. `NavigationRoutes.kt` is more feature-rich and should be the single source of truth.
- **Hardcoded Routes:** In `LoginScreen.kt` and `DashboardScreen.kt`, the navigation routes are hardcoded strings (e.g., `"dashboard"`, `"detection_list"`). They should be using the `NavigationRoutes` sealed class to navigate.
- **Argument Handling:** The `detailType` argument for the `Details` route is handled correctly in `NavigationGraph.kt`.
- **`NavHost` Usage:** The `NavHost` is set up correctly in `NavigationGraph.kt`.

## 8. Dependencies and Build Scripts

### Dependencies

- **Missing Dependencies:**
    - The project uses `itextpdf` and `poi` for report generation, but the dependencies are not declared in the `build.gradle.kts` file. Only `poi-ooxml` is present.
    - The `firebase-bom` platform is not used in the app-level `build.gradle.kts` file.
- **Unused Dependencies:** The following dependencies are likely unused:
    - `protolite-well-known-types`
    - `transportation-consumer`
    - `firebase-crashlytics-buildtools`
- **Duplicate Dependencies:** The `firebase-auth-ktx` dependency is declared twice with different versions.

### Build Scripts

- **ProGuard/R8:** ProGuard is disabled for release builds (`isMinifyEnabled = false`). This is a major security risk, as it leaves the code unobfuscated and vulnerable to reverse engineering.
- **Compose Compiler:** The `composeCompiler` version is not specified in the `composeOptions` block in the app-level `build.gradle.kts` file. This can lead to unexpected behavior and should be set explicitly.

## 9. Performance and Security

### Performance

- **Main Thread Work:**
    - `ReportGenerator.kt`: All report generation functions are synchronous and perform disk I/O on the main thread. This will block the UI and should be moved to a background thread.
    - `ImageClassifier.kt`: The TensorFlow Lite model is loaded on the main thread in the `init` block. This is a heavy operation and should be done asynchronously.
- **Recompositions:**
    - The data classes in the `models` directory are not annotated with `@Stable` or `@Immutable`, which can lead to unnecessary recompositions.
    - Objects like `AprilTagManager` and `FrameAnalyzer` are created inside composables, causing them to be recreated on every recomposition.

### Security

- **Obfuscation:** ProGuard/R8 is disabled for release builds (`isMinifyEnabled = false`). This is a major security risk that makes the app vulnerable to reverse engineering.
- **Insecure Storage:** `ReportGenerator.kt` saves reports to external storage, which is not secure. Sensitive data should be stored in the app's internal storage.

## 10. General Recommendations

### Refactoring

- **Remove Duplicate and Orphaned Code:** The first step should be to remove all the duplicate and orphaned files and functions that have been identified.
- **Consolidate Image Processing Logic:** The image processing logic in `ImageUtils.kt` and `FrameAnalyzer.kt` should be consolidated into a single, well-structured component.
- **Externalize Hardcoded Values:** All hardcoded strings, colors, and dimensions should be moved to resource files.
- **Fix Compilation Errors:** The compilation errors in `DetectionItemCard.kt` and `DetectionItemPreview.kt` should be fixed.
- **Correct Typos and Duplicated Fields:** The typo and duplicated field in `TraceabilityItem.kt` should be corrected.

### Architecture

- **Enforce Separation of Concerns:** The business logic in the UI layer should be moved to the ViewModels.
- **Break Down Large Classes:** The `FrameAnalyzer.kt` class should be broken down into smaller, more focused components.
- **Use Hilt Consistently:** `hiltViewModel()` should be used everywhere to retrieve ViewModels.

### Jetpack Compose

- **Hoist State to ViewModels:** The state that is currently managed in composables should be hoisted to the ViewModels.
- **Annotate Data Classes:** All data classes in the `models` directory should be annotated with `@Stable` or `@Immutable`.
- **Provide Objects from ViewModels:** Objects like `AprilTagManager` and `FrameAnalyzer` should be provided by ViewModels, rather than being created in composables.

### Performance and Security

- **Move Work Off the Main Thread:** The report generation and model loading should be moved to background threads.
- **Enable ProGuard/R8:** ProGuard/R8 should be enabled for release builds.
- **Use Internal Storage:** Sensitive data should be stored in the app's internal storage.

### Scaling

- **Add a Testing Strategy:** The project currently has no unit or integration tests. A testing strategy should be implemented to ensure the quality of the codebase.
- **Implement a CI/CD Pipeline:** A CI/CD pipeline would help to automate the build, test, and release process.
