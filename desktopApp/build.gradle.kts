import org.jetbrains.compose.desktop.application.dsl.TargetFormat

val maplibreNativeTarget: String by lazy {
    val override = project.findProperty("maplibreTarget") as? String
    if (override != null && override != "auto") override
    else {
        val hostOs = when (val os = System.getProperty("os.name").lowercase()) {
            "mac os x" -> "macos"
            else -> os.split(" ").first()
        }
        val hostArch = when (val arch = System.getProperty("os.arch").lowercase()) {
            "x86_64" -> "amd64"
            "arm64" -> "aarch64"
            else -> arch
        }
        val renderer = when (hostOs) {
            "macos" -> "metal"
            else -> "opengl"
        }
        "${hostOs}-${hostArch}-${renderer}"
    }
}

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

dependencies {
    implementation(projects.shared)

    implementation(compose.desktop.currentOs)
    implementation(libs.kotlinx.coroutinesSwing)

    implementation(libs.compose.uiToolingPreview)

    runtimeOnly(libs.maplibre.jni) {
        capabilities {
            requireCapability(
                "org.maplibre.compose:maplibre-native-bindings-jni-${maplibreNativeTarget}"
            )
        }
    }
}

compose.desktop {
    application {
        mainClass = "tech.sumato.avn.mp.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "tech.sumato.avn.mp"
            packageVersion = "1.0.0"

            macOS {
                infoPlist {
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
