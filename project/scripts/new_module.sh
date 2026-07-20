#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/../.." && pwd)"

usage() {
    echo "Usage: $0 <type> <name>"
    echo ""
    echo "Types:  domain, data, feature, core, component"
    echo ""
    echo "Examples:"
    echo "  $0 domain  farmer"
    echo "  $0 data    farmer"
    echo "  $0 feature farmer-registration"
    echo "  $0 core    database"
    echo "  $0 component map"
    exit 1
}

if [ $# -ne 2 ]; then
    usage
fi

TYPE="$1"
NAME="$2"

case "$TYPE" in
    domain|data|feature|core|component)
        ;;
    *)
        echo "Error: invalid type '$TYPE'. Allowed: domain, data, feature, core, component"
        usage
        ;;
esac

# --- derive names ---
MODULE_PATH="${TYPE}/${NAME}"
MODULE_ID=":${TYPE//\//:}:${NAME}"
PACKAGE_NAME="tech.sumato.avn.mp.${TYPE}.${NAME//-/_}"

# iOS framework baseName: e.g. "component_map" -> "ComponentMap"
BASENAME=$(echo "${TYPE}_${NAME}" | python3 -c "
import sys
s = sys.stdin.read().strip()
parts = s.replace('-', ' ').replace('_', ' ').split()
print(''.join(p.capitalize() for p in parts))
")

case "$TYPE" in
    domain|data|core)
        HAS_COMPOSE=false
        ;;
    feature|component)
        HAS_COMPOSE=true
        ;;
esac

# --- create directories ---
PACKAGE_DIR="tech/sumato/avn/mp/${TYPE}/${NAME//-/_}"
COMMON_SRC="${ROOT}/${MODULE_PATH}/src/commonMain/kotlin/${PACKAGE_DIR}"
ANDROID_SRC="${ROOT}/${MODULE_PATH}/src/androidMain/kotlin/${PACKAGE_DIR}"
mkdir -p "$COMMON_SRC"
mkdir -p "$ANDROID_SRC"

# --- write build.gradle.kts ---
BUILD_GRADLE="${ROOT}/${MODULE_PATH}/build.gradle.kts"

cat > "$BUILD_GRADLE" <<EOF
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
EOF

if [ "$HAS_COMPOSE" = true ]; then
    cat >> "$BUILD_GRADLE" <<EOF
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
EOF
fi

cat >> "$BUILD_GRADLE" <<EOF
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "${BASENAME}"
            isStatic = true
        }
    }

    jvm()
    js { browser() }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs { browser() }

    androidLibrary {
        namespace = "${PACKAGE_NAME}"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions { jvmTarget = JvmTarget.JVM_11 }
    }

    sourceSets {
        commonMain.dependencies {
EOF

case "$TYPE" in
    domain)
        cat >> "$BUILD_GRADLE" <<EOF
            implementation(projects.core.common)
            implementation(libs.kotlinx.coroutines.core)
EOF
        ;;
    data)
        cat >> "$BUILD_GRADLE" <<EOF
            implementation(projects.domain.xxx)
            implementation(projects.core.common)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.koin.core)
EOF
        ;;
    feature)
        cat >> "$BUILD_GRADLE" <<EOF
            implementation(projects.domain.xxx)
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
EOF
        ;;
    component)
        cat >> "$BUILD_GRADLE" <<EOF
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
EOF
        ;;
    core)
        cat >> "$BUILD_GRADLE" <<EOF
            implementation(libs.kotlinx.coroutines.core)
EOF
        ;;
esac

if [ "$HAS_COMPOSE" = true ]; then
    cat >> "$BUILD_GRADLE" <<EOF
            implementation(libs.compose.uiToolingPreview)
EOF
fi

cat >> "$BUILD_GRADLE" <<EOF
        }
    }
}
EOF

# --- write placeholder source file ---
cat > "${COMMON_SRC}/Placeholder.kt" <<EOF
package ${PACKAGE_NAME}

/**
 * TODO: Add your module's code here.
 */
object Placeholder
EOF

# --- add to settings.gradle.kts if not already present ---
SETTINGS="${ROOT}/settings.gradle.kts"
LINE="include(\"${MODULE_ID}\")"

if grep -qF "$LINE" "$SETTINGS"; then
    echo "Module ${MODULE_ID} already registered in settings.gradle.kts"
else
    sed -i '' "/^include(\":feature:map-analytics\")/a\\
${LINE}
" "$SETTINGS"
    echo "Added ${MODULE_ID} to settings.gradle.kts"
fi

echo ""
echo "✓ Module ${MODULE_ID} created at ${MODULE_PATH}/"
echo "  - build.gradle.kts"
echo "  - src/commonMain/.../Placeholder.kt"
echo ""
echo "Next steps:"
echo "  1. Open ${BUILD_GRADLE} and fill in the correct domain/data dependencies"
echo "  2. Replace Placeholder.kt with your actual code"
echo "  3. Run ./gradlew ${MODULE_ID//:/:}:compileKotlinJvm to verify"
