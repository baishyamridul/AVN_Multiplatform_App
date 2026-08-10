import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "DomainCommon"
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
        namespace = "tech.sumato.avn.mp.domain.common"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        compilerOptions { jvmTarget = JvmTarget.JVM_11 }
    }

    sourceSets {
        commonMain.dependencies {
        }
    }
}
