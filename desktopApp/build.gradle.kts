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
        "$hostOs-$hostArch-$renderer"
    }
}

/*
 * MapLibre stores native binaries in the JNI JAR using slash-separated paths:
 *   macos/aarch64/metal/libmaplibre-jni.dylib
 *   linux/amd64/opengl/libmaplibre-jni.so
 *   windows/amd64/opengl/maplibre-jni.dll
 */
val maplibreNativeJarPath: String by lazy {
    maplibreNativeTarget.replace('-', '/')
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

val bundleNativeDeps by tasks.registering {
    group = "distribution"
    description = "Patches and bundles MapLibre native dependencies into the packaged desktop app."

    dependsOn("createDistributable")

    /*
     * This task creates and runs OS-specific shell scripts against the generated
     * app bundle. Mark it explicitly so Gradle does not try to serialize this
     * script-heavy action into the configuration cache.
     */
    notCompatibleWithConfigurationCache("Runs generated shell scripts against Compose Desktop bundles.")

    val buildDir = layout.buildDirectory.get().asFile
    val packageAppName = appName
    val nativeJarPath = maplibreNativeJarPath

    doLast {
        fun File.findMaplibreJniJar(): File? =
            listFiles()?.firstOrNull { file ->
                file.name.contains("maplibre-native-bindings-jni") && file.name.endsWith(".jar")
            }

        fun writeExecutableScript(file: File, body: String) {
            file.writeText(body.trimIndent())
            file.setExecutable(true)
        }

        fun createMacosBundleScript(appOutputDir: File, tmpDir: File, logFile: File): File {
            val bundleDir = File(appOutputDir, "$packageAppName.app")
            val frameworksDir = File(bundleDir, "Contents/Frameworks")
            val appJarsDir = File(bundleDir, "Contents/app")
            val jniJar = appJarsDir.findMaplibreJniJar()
                ?: error("MapLibre JNI JAR not found in $appJarsDir")
            val nativeLibPath = "$nativeJarPath/libmaplibre-jni.dylib"
            val script = File(tmpDir, "bundle_macos.sh")

            writeExecutableScript(
                script,
                """
                #!/usr/bin/env bash
                set -euo pipefail

                BUNDLE_DIR="${bundleDir}"
                FW_DIR="${frameworksDir}"
                JNI_JAR="${jniJar}"
                NATIVE_LIB_PATH="${nativeLibPath}"
                TMP_DIR="${tmpDir}/maplibre_jni_patch"
                LOG="${logFile}"

                rm -rf "${'$'}{TMP_DIR}" "${'$'}{FW_DIR}"
                mkdir -p "${'$'}{TMP_DIR}" "${'$'}{FW_DIR}"
                cd "${'$'}{TMP_DIR}"

                # Extract the JNI dylib from the selected MapLibre native target.
                jar xf "${'$'}{JNI_JAR}" "${'$'}{NATIVE_LIB_PATH}" 2>>"${'$'}{LOG}"

                resign() {
                    codesign --force --sign - "${'$'}1" 2>>"${'$'}{LOG}" || true
                }

                process_dep() {
                    local lib_file="${'$'}1"
                    local deps
                    deps="$(otool -L "${'$'}{lib_file}" 2>/dev/null | grep '/opt/homebrew' | awk '{print ${'$'}1}')" || true

                    while IFS= read -r dep; do
                        [ -z "${'$'}{dep}" ] && continue

                        local base
                        base="$(basename "${'$'}{dep}")"

                        # Avoid absolute Homebrew paths in packaged apps.
                        install_name_tool -change "${'$'}{dep}" "@rpath/${'$'}{base}" "${'$'}{lib_file}" 2>>"${'$'}{LOG}" || true
                        resign "${'$'}{lib_file}"

                        if [ ! -f "${'$'}{FW_DIR}/${'$'}{base}" ]; then
                            cp "${'$'}{dep}" "${'$'}{FW_DIR}/${'$'}{base}" 2>>"${'$'}{LOG}"
                            install_name_tool -id "@rpath/${'$'}{base}" "${'$'}{FW_DIR}/${'$'}{base}" 2>>"${'$'}{LOG}" || true
                            resign "${'$'}{FW_DIR}/${'$'}{base}"
                            process_dep "${'$'}{FW_DIR}/${'$'}{base}"
                        fi
                    done <<< "${'$'}{deps}"
                }

                process_dep "${'$'}{TMP_DIR}/${'$'}{NATIVE_LIB_PATH}"
                resign "${'$'}{TMP_DIR}/${'$'}{NATIVE_LIB_PATH}"

                # Put the patched dylib back into the JNI JAR and re-sign the app bundle.
                jar uf "${'$'}{JNI_JAR}" -C "${'$'}{TMP_DIR}" "${'$'}{NATIVE_LIB_PATH}" 2>>"${'$'}{LOG}"
                codesign --force --deep --sign - "${'$'}{BUNDLE_DIR}" 2>>"${'$'}{LOG}"

                echo "bundleNativeDeps [macOS]: patched JAR + copied Frameworks + re-signed app"
                """
            )

            return script
        }

        fun createLinuxBundleScript(appOutputDir: File, tmpDir: File, logFile: File): File {
            val bundleDir = File(appOutputDir, packageAppName)
            val appJarsDir = File(bundleDir, "lib/app")
            val jreLibDir = File(bundleDir, "lib/runtime/lib")
            val jniJar = appJarsDir.findMaplibreJniJar()
                ?: error("MapLibre JNI JAR not found in $appJarsDir")
            val nativeLibPath = "$nativeJarPath/libmaplibre-jni.so"
            val script = File(tmpDir, "bundle_linux.sh")

            writeExecutableScript(
                script,
                """
                #!/usr/bin/env bash
                set -euo pipefail

                JNI_JAR="${jniJar}"
                JRE_LIB="${jreLibDir}"
                NATIVE_LIB_PATH="${nativeLibPath}"
                TMP_DIR="${tmpDir}/maplibre_jni_patch"
                LOG="${logFile}"

                rm -rf "${'$'}{TMP_DIR}"
                mkdir -p "${'$'}{TMP_DIR}"
                cd "${'$'}{TMP_DIR}"

                jar xf "${'$'}{JNI_JAR}" "${'$'}{NATIVE_LIB_PATH}" 2>>"${'$'}{LOG}"

                if command -v patchelf &>/dev/null; then
                    NATIVE_SO="${'$'}{TMP_DIR}/${'$'}{NATIVE_LIB_PATH}"
                    needed_deps="$(patchelf --print-needed "${'$'}{NATIVE_SO}")"

                    for dep in ${'$'}{needed_deps}; do
                        dep_path="$(find /usr/lib /lib /usr/local/lib -name "${'$'}{dep}" 2>/dev/null | head -1)" || true
                        [ -z "${'$'}{dep_path}" ] && continue

                        dep_base="$(basename "${'$'}{dep_path}")"
                        if [ ! -f "${'$'}{JRE_LIB}/${'$'}{dep_base}" ]; then
                            cp "${'$'}{dep_path}" "${'$'}{JRE_LIB}/${'$'}{dep_base}" 2>>"${'$'}{LOG}"
                            echo "bundleNativeDeps [Linux]: bundled ${'$'}{dep_base}" >>"${'$'}{LOG}"
                        fi
                    done

                    patchelf --set-rpath '${'$'}ORIGIN:'"${'$'}{JRE_LIB}" "${'$'}{NATIVE_SO}" 2>>"${'$'}{LOG}"
                    jar uf "${'$'}{JNI_JAR}" -C "${'$'}{TMP_DIR}" "${'$'}{NATIVE_LIB_PATH}" 2>>"${'$'}{LOG}"
                    echo "bundleNativeDeps [Linux]: patched RPATH + copied dependencies"
                else
                    echo "bundleNativeDeps [Linux]: patchelf not found, skipping RPATH patching" >>"${'$'}{LOG}"
                fi
                """
            )

            return script
        }

        fun createWindowsBundleScript(appOutputDir: File, tmpDir: File, logFile: File): File {
            val bundleDir = File(appOutputDir, packageAppName)
            val appJarsDir = File(bundleDir, "app")
            val jniJar = appJarsDir.findMaplibreJniJar()
                ?: error("MapLibre JNI JAR not found in $appJarsDir")
            val nativeLibPath = "$nativeJarPath/maplibre-jni.dll"
            val windowsTmpDir = "${tmpDir}\\maplibre_jni_patch"
            val script = File(tmpDir, "bundle_windows.bat")

            writeExecutableScript(
                script,
                """
                @echo off
                setlocal enabledelayedexpansion

                set "JNI_JAR=${jniJar}"
                set "NATIVE_LIB_PATH=${nativeLibPath}"
                set "TMP_DIR=${windowsTmpDir}"
                set "LOG=${logFile}"

                if exist "!TMP_DIR!" rmdir /s /q "!TMP_DIR!"
                mkdir "!TMP_DIR!"
                cd /d "!TMP_DIR!" || exit /b 1

                jar xf "!JNI_JAR!" "!NATIVE_LIB_PATH!" 2>>"!LOG!"
                jar uf "!JNI_JAR!" -C "!TMP_DIR!" "!NATIVE_LIB_PATH!" 2>>"!LOG!"

                echo bundleNativeDeps [Windows]: verified DLL in JNI JAR >>"!LOG!"
                """
            )

            return script
        }

        val osName = System.getProperty("os.name").lowercase()
        val appOutputDir = File(buildDir, "compose/binaries/main/app")
        val tmpDir = File(buildDir, "tmp/bundleNativeDeps").also { it.mkdirs() }
        val logFile = File(tmpDir, "bundle_native_deps.log").also { it.writeText("") }

        val script = when {
            osName.contains("mac") -> createMacosBundleScript(appOutputDir, tmpDir, logFile)
            osName.contains("linux") -> createLinuxBundleScript(appOutputDir, tmpDir, logFile)
            osName.contains("windows") -> createWindowsBundleScript(appOutputDir, tmpDir, logFile)
            else -> throw GradleException("bundleNativeDeps: unsupported OS: $osName")
        }

        val command = if (osName.contains("windows")) {
            listOf("cmd", "/c", script.absolutePath)
        } else {
            listOf("bash", script.absolutePath)
        }

        val exit = ProcessBuilder(command)
            .redirectOutput(ProcessBuilder.Redirect.INHERIT)
            .redirectError(ProcessBuilder.Redirect.to(logFile))
            .start()
            .waitFor()

        if (exit != 0) {
            if (logFile.exists()) println(logFile.readText())
            throw GradleException("bundleNativeDeps failed with exit code $exit")
        }
    }
}

/*
 * Compose creates the app first; this task then patches the generated bundle.
 * Keeping finalizedBy means normal packaging workflows also receive the native
 * dependency fix, while running bundleNativeDeps directly still works because it
 * depends on createDistributable.
 */
afterEvaluate {
    tasks.matching { it.name == "createDistributable" }.configureEach {
        finalizedBy(bundleNativeDeps)
    }
}

tasks.register<Exec>("runBundle") {
    group = "application"
    description = "Builds the macOS app bundle with native dependencies and opens it."

    dependsOn(bundleNativeDeps)

    val appDir = layout.buildDirectory.dir("compose/binaries/main/app/$appName.app")
    commandLine("open", appDir.get().asFile.absolutePath)
}

// Build for Intel macOS:
// ./gradlew :desktopApp:packageDmg -PmaplibreTarget=macos-amd64-metal -PjdkHome=/path/to/intel/jdk

