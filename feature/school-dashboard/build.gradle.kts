import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "FeatureSchoolDashboard"
            isStatic = true
        }
    }

    jvm {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_25
        }
    }

//    js { browser() }
//
//    @OptIn(ExperimentalWasmDsl::class)
//    wasmJs { browser() }

    androidLibrary {
        namespace = "tech.sumato.avn.mp.feature.school_dashboard"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        compilerOptions { jvmTarget = JvmTarget.JVM_11 }
        androidResources { enable = true }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)
            implementation(projects.core.navigation)
            implementation(projects.designsystem)

            implementation(projects.domain.common)
            implementation(projects.domain.school)
            implementation(projects.domain.user)

            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.kotlinx.coroutines.core)

            implementation(libs.material.icons)
            implementation(libs.material.icons.extended)

            implementation(libs.compose.uiToolingPreview)

            implementation(libs.kotlinx.serialization.json)

            implementation(projects.component.map)

            implementation(projects.component.image360)

            implementation(libs.kotlinx.date.time)

            implementation(libs.maplibre.compose)

//            implementation(libs.qrcode.kotlin)

            implementation(libs.qr.kit)

            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)

        }

        androidMain.dependencies {
            implementation(libs.maplibre.compose)
        }
        iosMain.dependencies {
        }
        jvmMain.dependencies {
            implementation(libs.maplibre.compose)
        }

    }
}
