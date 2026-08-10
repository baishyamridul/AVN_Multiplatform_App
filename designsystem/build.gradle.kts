import org.gradle.kotlin.dsl.invoke
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
            baseName = "DesignSystem"
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
        namespace = "tech.sumato.avn.mp.designsystem"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions { jvmTarget = JvmTarget.JVM_11 }
        androidResources { enable = true }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.material.icons.extended)


            implementation(libs.compose.uiToolingPreview)

            implementation(libs.kotlinx.date.time)

            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)

        }

        androidMain {
            dependencies {
                implementation(libs.ktor.client.android)
            }
        }
        iosMain {
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }
        jvmMain {
            dependencies {
                implementation(libs.ktor.client.java)
            }
        }

    }
}

compose.resources {

    publicResClass = true

}
