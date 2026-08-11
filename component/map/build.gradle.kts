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
            baseName = "ComponentMap"
            isStatic = true
        }
    }

    jvm {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_25
        }
    }

//    js { browser() }

//    @OptIn(ExperimentalWasmDsl::class)
//    wasmJs { browser() }

    androidLibrary {
        namespace = "tech.sumato.avn.mp.component.map"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        compilerOptions { jvmTarget = JvmTarget.JVM_11 }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.maplibre.compose)
            implementation(libs.compose.components.resources)
        }
        androidMain.dependencies {
//            implementation(libs.maplibre.compose)
        }
        iosMain.dependencies {
//            implementation(libs.maplibre.compose)
        }
        jvmMain.dependencies {
//            implementation(libs.maplibre.compose)
        }
    }
}
