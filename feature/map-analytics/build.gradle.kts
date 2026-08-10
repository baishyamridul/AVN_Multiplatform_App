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
            baseName = "FeatureMapAnalytics"
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
        namespace = "tech.sumato.avn.mp.feature.map_analytics"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        compilerOptions { jvmTarget = JvmTarget.JVM_11 }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.component.map)
            implementation(projects.core.common)
            implementation(projects.core.navigation)
            implementation(projects.designsystem)
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.material.icons.extended)
        }
        androidMain.dependencies {
            implementation(libs.maplibre.compose)
        }
        iosMain.dependencies {
        }
        jvmMain.dependencies {
            implementation(libs.maplibre.compose)
        }
//        jsMain.dependencies {
//        }
//        wasmJsMain.dependencies {
//        }
    }
}
