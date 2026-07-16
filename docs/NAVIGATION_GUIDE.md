# Navigation Guide — Screen-to-Screen Navigation

## Architecture

Per `navigation_rules.md`:

```
Features never navigate directly.

Features emit Effects.

Application layer performs navigation.
```

This means:

```
Feature Screen               App Layer (shared/)
       │                            │
       │  user taps "Login"         │
       ▼                            │
   ViewModel                        │
       │                            │
       │  emit NavigateToDashboard  │
       │  ──────────────────────►   │
       │                            │  observe Effect
       │                            │  call navController.navigate("dashboard")
       │                            ▼
       │                        NavHost composable
       │                        switches to DashboardScreen
```

**Where navigation lives:**

| Layer | Responsibility | Module |
|---|---|---|
| **Core** | Route constants, navigation infrastructure | `core/navigation/` |
| **Feature** | Emit navigation Effects (`LoginEffect.NavigateToDashboard`) | `feature/<name>/` |
| **App (shared)** | Observe Effects, perform `navController.navigate()` | `shared/` |

**Allowed/Forbidden:**

| Pattern | Status |
|---|---|
| Feature emits Effect → App navigates | ✅ |
| Feature calls `navController.navigate()` directly | ❌ Forbidden |
| Feature imports another feature's screen directly | ❌ Forbidden |
| Core defines route strings/constants | ✅ |
| App layer knows all routes and wires NavHost | ✅ |

---

## Step 1: Add Navigation Library

The project uses **JetBrains Navigation Compose** (`org.jetbrains.androidx.navigation:navigation-compose`) — the official KMP-compatible navigation library. It mirrors Jetpack Navigation Compose API.

Add to `gradle/libs.versions.toml`:

```toml
[versions]
androidx-navigation = "2.8.0-alpha10"  # JetBrains KMP version

[libraries]
androidx-navigation-compose = { module = "org.jetbrains.androidx.navigation:navigation-compose", version.ref = "androidx-navigation" }
```

The library is added to the `shared` module (or the module that hosts the root `NavHost`).

---

## Step 2: Create `core/navigation` Module (Route Definitions)

Per `gradle_module.md`, `core/navigation` is an independent module. It holds route constants and navigation contracts.

### `core/navigation/build.gradle.kts`

```kotlin
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
}

kotlin {
    listOf(iosArm64(), iosSimulatorArm64()).forEach { iosTarget ->
        iosTarget.binaries.framework { baseName = "CoreNavigation"; isStatic = true }
    }
    jvm()
    js { browser() }
    @OptIn(ExperimentalWasmDsl::class) wasmJs { browser() }

    androidLibrary {
        namespace = "tech.sumato.kmptemplate.core.navigation"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        compilerOptions { jvmTarget = JvmTarget.JVM_11 }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)
        }
    }
}
```

### Route Definitions

```kotlin
// core/navigation/src/commonMain/.../core/navigation/Route.kt
package tech.sumato.kmptemplate.core.navigation

object Route {
    const val LOGIN = "login"
    const val DASHBOARD = "dashboard"
    const val FARMER_REGISTRATION = "farmer_registration"
    const val FARMER_DETAILS = "farmer_details/{farmerId}"

    fun farmerDetails(farmerId: String) = "farmer_details/$farmerId"
}
```

Per `core_layer.md`, Core owns infrastructure only — routes are infrastructure (no business models, no UI, no ViewModels).

### Register in `settings.gradle.kts`

```kotlin
include(":core:navigation")
```

---

## Step 3: Feature Emits Navigation Effects

Each feature's `Effect` sealed interface already contains navigation actions.

### In `feature/login/presentation/LoginEffect.kt`:

```kotlin
sealed interface LoginEffect {
    data object NavigateToDashboard : LoginEffect
    data class ShowSnackbar(val message: String) : LoginEffect
}
```

### In `feature/dashboard/presentation/DashboardEffect.kt`:

```kotlin
sealed interface DashboardEffect {
    data object NavigateToLogin : DashboardEffect
    data object NavigateToFarmerRegistration : DashboardEffect
    data class ShowSnackbar(val message: String) : DashboardEffect
}
```

### ViewModel emits the effect:

```kotlin
// Inside LoginViewModel.login()
_state.value = LoginState.Success(result.user)
_effects.send(LoginEffect.NavigateToDashboard)
```

---

## Step 4: App Layer Observes Effects and Navigates

The `shared` module is the application layer. It:
1. Hosts the `NavHost` composable
2. Collects effects from the current screen's ViewModel
3. Calls `navController.navigate()` when a navigation effect fires

### `shared/build.gradle.kts` — add dependencies:

```kotlin
commonMain.dependencies {
    implementation(projects.core.navigation)
    implementation(libs.androidx.navigation.compose)
    // ... feature, data, domain deps
}
```

### `shared/src/commonMain/.../App.kt` — scaffold:

```kotlin
package tech.sumato.kmptemplate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import tech.sumato.kmptemplate.core.navigation.Route
import tech.sumato.kmptemplate.core.network.di.NetworkModule
import tech.sumato.kmptemplate.data.dashboard.di.DashboardDataModule
import tech.sumato.kmptemplate.data.user.di.UserDataModule
import tech.sumato.kmptemplate.designsystem.theme.KMPTemplateTheme
import tech.sumato.kmptemplate.feature.dashboard.di.DashboardFeatureModule
import tech.sumato.kmptemplate.feature.dashboard.presentation.DashboardEffect
import tech.sumato.kmptemplate.feature.dashboard.presentation.DashboardRoute
import tech.sumato.kmptemplate.feature.login.di.LoginFeatureModule
import tech.sumato.kmptemplate.feature.login.presentation.LoginEffect
import tech.sumato.kmptemplate.feature.login.presentation.LoginRoute

@Composable
fun App() {
    KoinApplication(application = {
        modules(
            NetworkModule,
            DashboardDataModule,
            DashboardFeatureModule,
            UserDataModule,
            LoginFeatureModule,
        )
    }) {
        KMPTemplateTheme {
            val navController = rememberNavController()
            AppNavGraph(navController = navController)
        }
    }
}

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Route.LOGIN,
    ) {
        composable(Route.LOGIN) {
            val viewModel = koinViewModel<LoginViewModel>()
            val state by viewModel.state.collectAsState()
            val effects = remember(viewModel) { viewModel.effects }

            LaunchedEffect(Unit) {
                effects.collect { effect ->
                    when (effect) {
                        is LoginEffect.NavigateToDashboard -> {
                            navController.navigate(Route.DASHBOARD) {
                                popUpTo(Route.LOGIN) { inclusive = true }
                            }
                        }
                        is LoginEffect.ShowSnackbar -> { /* show snackbar */ }
                    }
                }
            }

            LoginScreen(
                email = viewModel.email.collectAsState().value,
                password = viewModel.password.collectAsState().value,
                state = state,
                onEvent = viewModel::onEvent,
            )
        }

        composable(Route.DASHBOARD) {
            val viewModel = koinViewModel<DashboardViewModel>()
            val state by viewModel.state.collectAsState()
            val effects = remember(viewModel) { viewModel.effects }

            LaunchedEffect(Unit) {
                effects.collect { effect ->
                    when (effect) {
                        is DashboardEffect.NavigateToLogin -> {
                            navController.navigate(Route.LOGIN) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                        is DashboardEffect.NavigateToFarmerRegistration -> {
                            navController.navigate(Route.FARMER_REGISTRATION)
                        }
                        is DashboardEffect.ShowSnackbar -> { }
                    }
                }
            }

            DashboardScreen(state = state, onEvent = viewModel::onEvent)
        }

        composable(Route.FARMER_REGISTRATION) {
            // FarmerRegistrationRoute()
        }

        composable(Route.FARMER_DETAILS) { backStackEntry ->
            val farmerId = backStackEntry.arguments?.getString("farmerId")
            // FarmerDetailsRoute(farmerId = farmerId)
        }
    }
}
```

---

## Step 5: Passing Arguments Between Screens

Use route arguments via `Route` constants:

```kotlin
// core/navigation/Route.kt
object Route {
    const val FARMER_DETAILS = "farmer_details/{farmerId}"

    fun farmerDetails(farmerId: String) = "farmer_details/$farmerId"
}
```

In `NavHost`:

```kotlin
composable(Route.FARMER_DETAILS) { backStackEntry ->
    val farmerId = backStackEntry.arguments?.getString("farmerId")
    // Inject into ViewModel or pass to screen
}
```

---

## Step 6: Navigation Effect Pattern (Reference)

Copy this pattern for every new feature:

### 1. Define the navigation effect in your feature's Effect file:

```kotlin
sealed interface MyFeatureEffect {
    data object NavigateToNextScreen : MyFeatureEffect
    data object NavigateBack : MyFeatureEffect
    data class ShowSnackbar(val message: String) : MyFeatureEffect
}
```

### 2. Emit it from ViewModel:

```kotlin
_effects.send(MyFeatureEffect.NavigateToNextScreen)
```

### 3. Handle it in the AppNavGraph:

```kotlin
composable(Route.MY_FEATURE) {
    // ... viewModel setup
    LaunchedEffect(Unit) {
        effects.collect { effect ->
            when (effect) {
                is MyFeatureEffect.NavigateToNextScreen -> {
                    navController.navigate(Route.NEXT_SCREEN)
                }
                is MyFeatureEffect.NavigateBack -> {
                    navController.popBackStack()
                }
                is MyFeatureEffect.ShowSnackbar -> { }
            }
        }
    }
}
```

---

## Summary: Navigation Data Flow

```
User taps button
       │
       ▼
Screen calls viewModel.onEvent(MyEvent.ButtonClicked)
       │
       ▼
ViewModel executes business logic (use case / repository)
       │
       ▼
ViewModel emits _effects.send(MyEffect.NavigateToScreen)
       │
       ▼
AppNavGraph's LaunchedEffect collects the effect
       │
       ▼
navController.navigate(Route.SCREEN)
       │
       ▼
NavHost switches to the target composable
```

## Verification Checklist

```
☐ Navigation module exists: core/navigation with Route constants
☐ Routes are defined as string constants (no magic strings in features)
☐ Features emit navigation Effects (never call navController)
☐ AppNavGraph in shared/ observes effects and calls navController.navigate()
☐ NavHost is set up with all screen composables
☐ All feature DI modules registered in App()
☐ core/navigation registered in settings.gradle.kts
☐ shared/build.gradle.kts has navigation-compose dep + core/navigation dep
☐ Arguments passed via route params (not shared state)
☐ Back stack managed properly (popUpTo, inclusive)
```
