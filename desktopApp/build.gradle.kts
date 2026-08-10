/*
 * Desktop application build.
 *
 * The ordinary Compose Desktop setup is small; most of this file exists because
 * MapLibre ships native JNI libraries that need extra care when packaged into
 * self-contained desktop distributions.
 */


import org.jetbrains.compose.desktop.application.dsl.TargetFormat

val appName = "Arunachal Vidya Nidhi"

val maplibreVersion = libs.versions.maplibre.get()

/*
 * MapLibre Compose 0.14.0 ships native libraries as platform runtime modules
 * (FFM-based FFI, requires Java 25). Select the module matching this build host.
 */
val maplibreRuntimeArtifact: String by lazy {
    val hostOs = when (val os = System.getProperty("os.name").lowercase()) {
        "mac os x" -> "macos"
        "linux" -> "linux"
        "windows" -> "windows"
        else -> error("maplibreRuntimeArtifact: unsupported OS: $os")
    }
    val hostArch = when (val arch = System.getProperty("os.arch").lowercase()) {
        "x86_64", "amd64" -> "x64"
        "arm64", "aarch64" -> "arm64"
        else -> arch
    }
    val renderer = if (hostOs == "macos") "metal" else "vulkan"
    "maplibre-compose-runtime-$renderer-$hostOs-$hostArch"
}

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_25
    }
}

/*
 * The Compose Hot Reload launcher defaults to a JetBrains Runtime 21, which cannot
 * load the Java 25 bytecode this project compiles to. Point it at the same JDK the
 * Gradle daemon runs on (the value of org.gradle.java.home).
 */
if (!providers.gradleProperty("compose.reload.jbr.binary").isPresent) {
    System.setProperty("compose.reload.jbr.binary", "${System.getProperty("java.home")}/bin/java")
}

dependencies {
    implementation(projects.shared)

    implementation(compose.desktop.currentOs)
    implementation(libs.kotlinx.coroutinesSwing)
    implementation(libs.maplibre.compose)

    implementation(libs.compose.uiToolingPreview)

    runtimeOnly("org.maplibre.compose:$maplibreRuntimeArtifact:$maplibreVersion")
}

compose.desktop {
    application {
        mainClass = "tech.sumato.avn.mp.MainKt"

        /*
         * MapLibre Native FFI (0.14.0) uses the Java FFM API, which needs explicit
         * native access when running on the JVM.
         */
        jvmArgs += "--enable-native-access=ALL-UNNAMED"

        /*
        * Useful for packaging with a specific JDK, for example when building an
        * Intel macOS package from an Apple Silicon machine.
        */
        (project.findProperty("jdkHome") as? String)?.let { javaHome = it }

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = appName
            packageVersion = "1.0.0"

            macOS {
                infoPlist {
                    /*
                     * The current map style uses several HTTP tile/vector URLs.
                     * Without this, macOS App Transport Security can block them.
                     */
                    extraKeysRawXml = """
                        <key>NSAppTransportSecurity</key>
                        <dict>
                            <key>NSAllowsArbitraryLoads</key>
                            <true/>
                        </dict>
                    """.trimIndent()
                }
            }
        }
    }
}

// Build for Intel macOS (packaging only):
// ./gradlew :desktopApp:packageDmg -PjdkHome=/path/to/intel/jdk

