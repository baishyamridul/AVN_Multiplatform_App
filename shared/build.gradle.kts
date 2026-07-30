import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
//    alias(libs.plugins.kotlinCocoapods)
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    jvm()
//    js { browser() }

//    @OptIn(ExperimentalWasmDsl::class)
//    wasmJs { browser() }

    androidLibrary {
       namespace = "tech.sumato.avn.mp.shared"
       compileSdk = libs.versions.android.compileSdk.get().toInt()
       minSdk = libs.versions.android.minSdk.get().toInt()

       compilerOptions {
           jvmTarget = JvmTarget.JVM_11
       }
       androidResources {
           enable = true
       }
       withHostTest {
           isIncludeAndroidResources = true
       }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.feature.dashboard)
            implementation(projects.feature.login)
            implementation(projects.feature.mapAnalytics)
            implementation(projects.data.dashboard)
            implementation(projects.data.user)
            implementation(projects.domain.user)
            implementation(projects.core.network)
            implementation(projects.designsystem)
            implementation(projects.core.common)
            implementation(projects.core.navigation)

            implementation(projects.feature.districtDashboard)
            implementation(projects.feature.schoolDashboard)
            implementation(projects.data.districtDashboard)

            implementation(libs.androidx.navigation.compose)

            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)

            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            implementation(libs.material.icons)
            implementation(libs.material.icons.extended)

            implementation(libs.kotlinx.date.time)

        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
        }
//        jsMain.dependencies {
//            implementation(libs.wrappers.browser)
//        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}
