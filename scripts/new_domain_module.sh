#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"

usage() {
    echo "Usage: $0 <name>"
    echo ""
    echo "Creates a new domain module under domain/<name>/"
    echo ""
    echo "Examples:"
    echo "  $0 school"
    echo "  $0 farmer"
    echo "  $0 weather"
    exit 1
}

if [ $# -ne 1 ]; then
    usage
fi

NAME="$1"
NAME_SNAKE="${NAME//-/_}"
MODULE_ID=":domain:${NAME}"
PACKAGE_NAME="tech.sumato.avn.mp.domain.${NAME_SNAKE}"
BASENAME="Domain$(echo "$NAME" | sed -E 's/(^|-)([a-z])/\U\2/g')"

# --- create directories ---
PACKAGE_DIR="tech/sumato/avn/mp/domain/${NAME_SNAKE}"
mkdir -p "${ROOT}/domain/${NAME}/src/commonMain/kotlin/${PACKAGE_DIR}/model"
mkdir -p "${ROOT}/domain/${NAME}/src/commonMain/kotlin/${PACKAGE_DIR}/repository"
mkdir -p "${ROOT}/domain/${NAME}/src/commonMain/kotlin/${PACKAGE_DIR}/usecase"

# --- write build.gradle.kts ---
cat > "${ROOT}/domain/${NAME}/build.gradle.kts" <<EOF
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
            implementation(projects.core.common)
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}
EOF

# --- write placeholder files ---

cat > "${ROOT}/domain/${NAME}/src/commonMain/kotlin/${PACKAGE_DIR}/model/Placeholder.kt" <<EOF
package ${PACKAGE_NAME}.model

data class PlaceholderModel(
    val id: String,
    val name: String,
)
EOF

cat > "${ROOT}/domain/${NAME}/src/commonMain/kotlin/${PACKAGE_DIR}/repository/PlaceholderRepository.kt" <<EOF
package ${PACKAGE_NAME}.repository

import ${PACKAGE_NAME}.model.PlaceholderModel

interface PlaceholderRepository {
    suspend fun getData(): List<PlaceholderModel>
}
EOF

cat > "${ROOT}/domain/${NAME}/src/commonMain/kotlin/${PACKAGE_DIR}/usecase/PlaceholderUseCase.kt" <<EOF
package ${PACKAGE_NAME}.usecase

import ${PACKAGE_NAME}.model.PlaceholderModel
import ${PACKAGE_NAME}.repository.PlaceholderRepository

class PlaceholderUseCase(
    private val repository: PlaceholderRepository,
) {
    suspend operator fun invoke(): List<PlaceholderModel> = repository.getData()
}
EOF

# --- add to settings.gradle.kts ---
SETTINGS="${ROOT}/settings.gradle.kts"
LINE="include(\"${MODULE_ID}\")"

if grep -qF "$LINE" "$SETTINGS"; then
    echo "Module ${MODULE_ID} already registered in settings.gradle.kts"
else
    sed -i '' "/^include(\":data:user\")/a\\
${LINE}
" "$SETTINGS"
    echo "Added ${MODULE_ID} to settings.gradle.kts"
fi

echo ""
echo "✓ Domain module ${MODULE_ID} created at domain/${NAME}/"
echo ""
echo "Files created:"
echo "  domain/${NAME}/build.gradle.kts"
echo "  domain/${NAME}/src/commonMain/.../model/Placeholder.kt"
echo "  domain/${NAME}/src/commonMain/.../repository/PlaceholderRepository.kt"
echo "  domain/${NAME}/src/commonMain/.../usecase/PlaceholderUseCase.kt"
echo ""
echo "Next steps:"
echo "  1. Rename PlaceholderModel, PlaceholderRepository, PlaceholderUseCase to match your domain"
echo "  2. Run ./gradlew ${MODULE_ID}:compileKotlinJvm to verify"
