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
            baseName = "CoreDataStore"
            isStatic = true
        }
    }

    jvm()

    androidLibrary {
        namespace = "tech.sumato.avn.mp.core.datastore"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions { jvmTarget = JvmTarget.JVM_11 }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.koin.core)
//            implementation(libs.datastore.preferences.core)
            implementation(libs.datastore)
            implementation(libs.datastore.preferences)
        }
        androidMain.dependencies {
        }
        iosMain.dependencies {
        }
        jvmMain.dependencies {
        }
    }
}
