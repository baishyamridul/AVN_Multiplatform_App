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

val hostOs = when (val os = System.getProperty("os.name").lowercase()) {
    "mac os x" -> "macos"
    "linux" -> "linux"
    "windows" -> "windows"
    else -> error("unsupported OS: $os")
}
val hostArch = when (val arch = System.getProperty("os.arch").lowercase()) {
    "x86_64", "amd64" -> "x64"
    "arm64", "aarch64" -> "arm64"
    else -> arch
}

/*
 * MapLibre Compose 0.14.0 ships native libraries as platform runtime modules
 * (FFM-based FFI, requires Java 25). Select the module matching this build host.
 */
val maplibreRuntimeArtifact: String by lazy {
    val renderer = if (hostOs == "macos") "metal" else "vulkan"
    "maplibre-compose-runtime-$renderer-$hostOs-$hostArch"
}

/*
 * Bytedeco (via saralapps webview -> javacv-platform) pulls natives for every
 * OS/arch into Contents/app, bloating the DMG to ~1GB. Keep only the classifiers
 * for the current packaging host. Mac is universal (both arm64 + x64).
 */
val allowedBytedecoClassifiers: Set<String> = when (hostOs) {
    "macos" -> setOf("macosx-arm64", "macosx-x86_64")
    "windows" -> setOf("windows-x86_64", "windows-x86")
    "linux" -> setOf("linux-x86_64", "linux-arm64")
    else -> emptySet()
}
val isBytedecoJar: (String) -> Boolean = { name ->
    name.startsWith("opencv-") || name.startsWith("ffmpeg-") || name.startsWith("openblas-") ||
        name.startsWith("javacpp-") || name.startsWith("javacv-") ||
        name.startsWith("flycapture-") || name.startsWith("libdc1394-") ||
        name.startsWith("libfreenect") || name.startsWith("librealsense") ||
        name.startsWith("videoinput-")
}
val isUnwantedBytedecoJar: (String) -> Boolean = { name ->
    if (!isBytedecoJar(name)) false
    else {
        // Keep jars without a platform classifier (e.g. javacpp-1.5.8.jar) — they are the core
        val hasClassifier = allowedBytedecoClassifiers.any { name.contains(it) } ||
            name.contains("windows-") || name.contains("linux-") || name.contains("macosx-") ||
            name.contains("android-") || name.contains("ios-")
        if (!hasClassifier) false else allowedBytedecoClassifiers.none { name.contains(it) }
    }
}

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    jvmToolchain(25)
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
            targetFormats(
                *when (hostOs) {
                    "macos" -> arrayOf(TargetFormat.Dmg)
                    "windows" -> arrayOf(TargetFormat.Msi)
                    else -> arrayOf(TargetFormat.Deb)
                }
            )
            packageName = appName
            packageVersion = "1.1.0"

            /*
             * DataStore's protobuf-lite hard-references sun.misc.Unsafe, which lives
             * in the jdk.unsupported module. jlink trims it out of the packaged runtime
             * by default, causing NoClassDefFoundError: sun/misc/Unsafe on first write.
             */
//            modules("jdk.unsupported")
            modules("java.sql", "java.naming", "jdk.crypto.ec", "jdk.unsupported")

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

            // 5. Windows-specific configurations
            windows {
                menu = true
                shortcut = true
                // To sign windows apps via gradle, or use a custom tool post-build
            }

            // 6. ProGuard rules for performance, size shrinking, and obfuscation
            buildTypes.release.proguard {
                isEnabled.set(true)
                optimize.set(true)
                obfuscate.set(true)
                configurationFiles.from(project.file("compose-desktop.pro"))
            }

        }
    }
}

// Strip bytedeco natives for non-target OSs after packaging (saves ~700-800MB).
// Keeps Mac universal (both arm64 + x64), host-specific for Windows/Linux.
tasks.matching {
    it.name in setOf(
        "createDistributable", "packageAppImage", "packageDmg", "packageMsi", "packageDeb",
        "packageDistributionForCurrentOS", "createDistributableForCurrentOS"
    )
}.configureEach {
    notCompatibleWithConfigurationCache("Filters bytedeco jars using hostOs at execution time")
    doLast {
        val os = System.getProperty("os.name").lowercase().let {
            when {
                it.contains("mac") -> "macos"
                it.contains("win") -> "windows"
                else -> "linux"
            }
        }
        val allowed = when (os) {
            "macos" -> setOf("macosx-arm64", "macosx-x86_64")
            "windows" -> setOf("windows-x86_64", "windows-x86")
            else -> setOf("linux-x86_64", "linux-arm64")
        }
        fun isBytedeco(n: String) = n.startsWith("opencv-") || n.startsWith("ffmpeg-") ||
            n.startsWith("openblas-") || n.startsWith("javacpp-") || n.startsWith("javacv-") ||
            n.startsWith("flycapture-") || n.startsWith("libdc1394-") ||
            n.startsWith("libfreenect") || n.startsWith("librealsense") || n.startsWith("videoinput-")
        fun isUnwanted(n: String): Boolean {
            if (!isBytedeco(n)) return false
            val hasClassifier = allowed.any { n.contains(it) } ||
                n.contains("windows-") || n.contains("linux-") || n.contains("macosx-") ||
                n.contains("android-") || n.contains("ios-")
            return hasClassifier && allowed.none { n.contains(it) }
        }
        val appDir = layout.buildDirectory.dir("compose/binaries/main/app/$appName.app/Contents/app").get().asFile
        if (!appDir.exists()) return@doLast
        val toRemove = appDir.listFiles { f -> isUnwanted(f.name) }?.toList() ?: return@doLast
        if (toRemove.isEmpty()) return@doLast
        toRemove.forEach { it.delete() }
        println("Filtered ${toRemove.size} bytedeco jars for $os (kept $allowed)")
    }
}

// Build for Intel macOS (packaging only):
// ./gradlew :desktopApp:packageDmg -PjdkHome=/path/to/intel/jdk

