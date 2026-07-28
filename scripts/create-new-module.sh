#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/../" && pwd)"
NAMESPACE="tech.sumato.avn.mp"

die() { echo "Error: $*" >&2; exit 1; }

usage() {
    cat <<EOF
Usage: $(basename "$0") <type> <module-name>

Types:
  domain    — Business models, repository interfaces, use cases
  data      — DTOs, API clients, mappers, repository impl, DI
  feature   — Screen, ViewModel, State, Event, Effect, Route, DI
  core      — Infrastructure: network, database, navigation, etc.
  component — Reusable KMP composable (iOS + Android + JVM, Compose)

Examples:
  $(basename "$0") domain  farmer
  $(basename "$0") data    farmer
  $(basename "$0") feature farmer-registration
  $(basename "$0") core    network
  $(basename "$0") component map
EOF
    exit 1
}

[[ $# -ne 2 ]] && usage

TYPE="$1"
RAW_NAME="$2"

case "$TYPE" in
    domain|data|feature|core|component) ;;
    *) die "Invalid type '$TYPE'. Allowed: domain, data, feature, core, component" ;;
esac

# --- name derivation ---
LOWERCASE=$(echo "$RAW_NAME" | tr '[:upper:]' '[:lower:]')
PACKAGE_SEGMENT="${LOWERCASE//-/_}"

to_upper_camel() {
    local word="$1"
    while [[ "$word" =~ -([a-z]) ]]; do
        local char="${BASH_REMATCH[1]}"
        local upper=$(echo "$char" | tr '[:lower:]' '[:upper:]')
        word="${word/-$char/$upper}"
    done
    local first=$(echo "${word:0:1}" | tr '[:lower:]' '[:upper:]')
    echo "${first}${word:1}"
}

CAPITALIZED=$(to_upper_camel "$LOWERCASE")
PACKAGE="$NAMESPACE.$TYPE.$PACKAGE_SEGMENT"
MODULE_ID=":$TYPE:$LOWERCASE"
MODULE_DIR="$ROOT/$TYPE/$LOWERCASE"

# iOS framework baseName: e.g. "farmer_registration" -> "FarmerRegistration"
ios_base_name() {
    local t="$1" n="$2"
    echo "${t}_${n}" | python3 -c "
import sys
s = sys.stdin.read().strip()
parts = s.replace('-', ' ').replace('_', ' ').split()
print(''.join(p.capitalize() for p in parts))
"
}
BASENAME=$(ios_base_name "$TYPE" "$LOWERCASE")

# Platform targets
case "$TYPE" in
    domain|data|core)
        HAS_COMPOSE=false
        HAS_JS=true
        ;;
    feature)
        HAS_COMPOSE=true
        HAS_JS=true
        ;;
    component)
        HAS_COMPOSE=true
        HAS_JS=false
        ;;
esac

# --- validation ---
[[ -d "$MODULE_DIR" ]] && die "Module directory already exists: $MODULE_DIR"

# --- helpers ---
pkg_path() { echo "$MODULE_DIR/src/$1/kotlin/${PACKAGE//./.}"; }

# ======================================================================
# 1. build.gradle.kts
# ======================================================================
write_build_gradle() {
    local file="$MODULE_DIR/build.gradle.kts"
    mkdir -p "$(dirname "$file")"

    local plugins=""
    local deps=""

    # Plugins
    case "$TYPE" in
        domain|core)
            plugins=$(cat <<-PLUG
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
PLUG
)
            ;;
        data)
            plugins=$(cat <<-PLUG
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.kotlinxSerialization)
PLUG
)
            ;;
        feature|component)
            plugins=$(cat <<-PLUG
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
PLUG
)
            ;;
    esac

    # Dependencies
    case "$TYPE" in
        core)
            deps=$(cat <<-DEP
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
        }
DEP
)
            ;;
        domain)
            deps=$(cat <<-DEP
        commonMain.dependencies {
            implementation(projects.core.common)
            implementation(libs.kotlinx.coroutines.core)
        }
DEP
)
            ;;
        data)
            deps=$(cat <<-DEP
        commonMain.dependencies {
            implementation(projects.core.common)
            implementation(projects.core.network)
            implementation(projects.domain.$LOWERCASE)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.koin.core)
            implementation(libs.ktor.client.core)
        }
DEP
)
            ;;
        feature)
            deps=$(cat <<-DEP
        commonMain.dependencies {
            implementation(projects.core.common)
            implementation(projects.core.navigation)
            implementation(projects.designsystem)
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.kotlinx.coroutines.core)
        }
DEP
)
            ;;
        component)
            deps=$(cat <<-DEP
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.kotlinx.coroutines.core)
        }
DEP
)
            ;;
    esac

    # JS/Wasm targets
    local extra_targets=""
    if [[ "$HAS_JS" == true ]]; then
        extra_targets=$(cat <<-TARGETS
    js { browser() }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs { browser() }
TARGETS
)
    fi

    cat > "$file" <<-BUILD
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
$plugins
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "$BASENAME"
            isStatic = true
        }
    }

    jvm()
$extra_targets

    androidLibrary {
        namespace = "$PACKAGE"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        compilerOptions { jvmTarget = JvmTarget.JVM_11 }
    }

    sourceSets {
$deps
    }
}
BUILD
    echo "   - build.gradle.kts"
}

# ======================================================================
# 2. Source files per type
# ======================================================================
write_core_sources() {
    local dir=$(pkg_path "commonMain")
    mkdir -p "$dir"

    cat > "$dir/Placeholder.kt" <<-KT
package $PACKAGE

object Placeholder
KT
    echo "   - src/commonMain/.../Placeholder.kt"
}

write_domain_sources() {
    local base=$(pkg_path "commonMain")

    mkdir -p "$base/model" "$base/repository" "$base/usecase"

    # Model
    cat > "$base/model/${CAPITALIZED}.kt" <<-KT
package $PACKAGE.model

data class $CAPITALIZED(
    val id: String,
)
KT

    # Repository interface
    cat > "$base/repository/${CAPITALIZED}Repository.kt" <<-KT
package $PACKAGE.repository

import $PACKAGE.model.$CAPITALIZED

interface ${CAPITALIZED}Repository {
    suspend fun get(): $CAPITALIZED
}
KT

    # Use case
    cat > "$base/usecase/Get${CAPITALIZED}UseCase.kt" <<-KT
package $PACKAGE.usecase

import $PACKAGE.repository.${CAPITALIZED}Repository

class Get${CAPITALIZED}UseCase(
    private val repository: ${CAPITALIZED}Repository,
) {
    suspend operator fun invoke() = repository.get()
}
KT

    echo "   - src/commonMain/.../model/${CAPITALIZED}.kt"
    echo "   - src/commonMain/.../repository/${CAPITALIZED}Repository.kt"
    echo "   - src/commonMain/.../usecase/Get${CAPITALIZED}UseCase.kt"
}

write_data_sources() {
    local base=$(pkg_path "commonMain")

    mkdir -p "$base/remote" "$base/mapper" "$base/repository" "$base/di"

    local domain_pkg="$NAMESPACE.domain.$PACKAGE_SEGMENT"

    # DTO
    cat > "$base/remote/${CAPITALIZED}Dto.kt" <<-KT
package $PACKAGE.remote

import kotlinx.serialization.Serializable

@Serializable
data class ${CAPITALIZED}Dto(
    val id: String,
)
KT

    # API
    cat > "$base/remote/${CAPITALIZED}Api.kt" <<-KT
package $PACKAGE.remote

import io.ktor.client.HttpClient

class ${CAPITALIZED}Api(
    private val httpClient: HttpClient,
)
KT

    # Mapper
    cat > "$base/mapper/${CAPITALIZED}Mapper.kt" <<-KT
package $PACKAGE.mapper

import $PACKAGE.remote.${CAPITALIZED}Dto
import $domain_pkg.model.$CAPITALIZED

class ${CAPITALIZED}Mapper {
    fun toDomain(dto: ${CAPITALIZED}Dto): $CAPITALIZED {
        return $CAPITALIZED(
            id = dto.id,
        )
    }
}
KT

    # Repository impl
    cat > "$base/repository/${CAPITALIZED}RepositoryImpl.kt" <<-KT
package $PACKAGE.repository

import $PACKAGE.remote.${CAPITALIZED}Api
import $PACKAGE.mapper.${CAPITALIZED}Mapper
import $domain_pkg.model.$CAPITALIZED
import $domain_pkg.repository.${CAPITALIZED}Repository

class ${CAPITALIZED}RepositoryImpl(
    private val api: ${CAPITALIZED}Api,
    private val mapper: ${CAPITALIZED}Mapper,
) : ${CAPITALIZED}Repository {

    override suspend fun get(): $CAPITALIZED {
        TODO("implement")
    }
}
KT

    # DI module
    cat > "$base/di/${CAPITALIZED}DataModule.kt" <<-KT
package $PACKAGE.di

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import $PACKAGE.mapper.${CAPITALIZED}Mapper
import $PACKAGE.remote.${CAPITALIZED}Api
import $PACKAGE.repository.${CAPITALIZED}RepositoryImpl
import $domain_pkg.repository.${CAPITALIZED}Repository

val ${CAPITALIZED}DataModule = module {
    singleOf(::${CAPITALIZED}Api)
    singleOf(::${CAPITALIZED}Mapper)
    singleOf(::${CAPITALIZED}RepositoryImpl) { bind<${CAPITALIZED}Repository>() }
}
KT

    echo "   - src/commonMain/.../remote/${CAPITALIZED}Dto.kt"
    echo "   - src/commonMain/.../remote/${CAPITALIZED}Api.kt"
    echo "   - src/commonMain/.../mapper/${CAPITALIZED}Mapper.kt"
    echo "   - src/commonMain/.../repository/${CAPITALIZED}RepositoryImpl.kt"
    echo "   - src/commonMain/.../di/${CAPITALIZED}DataModule.kt"
}

write_feature_sources() {
    local base=$(pkg_path "commonMain")

    mkdir -p "$base/presentation" "$base/di"

    # State
    cat > "$base/presentation/${CAPITALIZED}State.kt" <<-KT
package $PACKAGE.presentation

sealed interface ${CAPITALIZED}State {
    data object Loading : ${CAPITALIZED}State
    data object Idle : ${CAPITALIZED}State
    data class Error(val message: String) : ${CAPITALIZED}State
}
KT

    # Event
    cat > "$base/presentation/${CAPITALIZED}Event.kt" <<-KT
package $PACKAGE.presentation

sealed interface ${CAPITALIZED}Event
KT

    # Effect
    cat > "$base/presentation/${CAPITALIZED}Effect.kt" <<-KT
package $PACKAGE.presentation

sealed interface ${CAPITALIZED}Effect {
    data class ShowSnackbar(val message: String) : ${CAPITALIZED}Effect
}
KT

    # ViewModel
    cat > "$base/presentation/${CAPITALIZED}ViewModel.kt" <<-KT
package $PACKAGE.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import $NAMESPACE.core.navigation.MviViewModel

class ${CAPITALIZED}ViewModel : ViewModel(), MviViewModel<${CAPITALIZED}State, ${CAPITALIZED}Effect> {

    private val _state = MutableStateFlow<${CAPITALIZED}State>(${CAPITALIZED}State.Loading)
    override val state: StateFlow<${CAPITALIZED}State> = _state.asStateFlow()

    private val _effects = Channel<${CAPITALIZED}Effect>(Channel.BUFFERED)
    override val effects: Flow<${CAPITALIZED}Effect> = _effects.receiveAsFlow()

    fun onEvent(event: ${CAPITALIZED}Event) {
        when (event) { }
    }
}
KT

    # Screen
    cat > "$base/presentation/${CAPITALIZED}Screen.kt" <<-KT
package $PACKAGE.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ${CAPITALIZED}Screen(
    state: ${CAPITALIZED}State,
    onEvent: (${CAPITALIZED}Event) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text("${CAPITALIZED}")
    }
}
KT

    # Route
    cat > "$base/presentation/${CAPITALIZED}Route.kt" <<-KT
package $PACKAGE.presentation

import androidx.compose.runtime.Composable
import $NAMESPACE.core.navigation.BaseRoute

@Composable
fun ${CAPITALIZED}Route(
    onShowSnackbar: (String) -> Unit = {},
) {
    BaseRoute<${CAPITALIZED}ViewModel, ${CAPITALIZED}State, ${CAPITALIZED}Effect>(
        onEffect = { effect ->
            when (effect) {
                is ${CAPITALIZED}Effect.ShowSnackbar -> onShowSnackbar(effect.message)
            }
        },
        content = { state ->
            ${CAPITALIZED}Screen(
                state = state,
                onEvent = ::onEvent,
            )
        },
    )
}
KT

    # DI
    cat > "$base/di/${CAPITALIZED}FeatureModule.kt" <<-KT
package $PACKAGE.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import $PACKAGE.presentation.${CAPITALIZED}ViewModel

val ${CAPITALIZED}FeatureModule = module {
    viewModelOf(::${CAPITALIZED}ViewModel)
}
KT

    echo "   - src/commonMain/.../presentation/${CAPITALIZED}State.kt"
    echo "   - src/commonMain/.../presentation/${CAPITALIZED}Event.kt"
    echo "   - src/commonMain/.../presentation/${CAPITALIZED}Effect.kt"
    echo "   - src/commonMain/.../presentation/${CAPITALIZED}ViewModel.kt"
    echo "   - src/commonMain/.../presentation/${CAPITALIZED}Screen.kt"
    echo "   - src/commonMain/.../presentation/${CAPITALIZED}Route.kt"
    echo "   - src/commonMain/.../di/${CAPITALIZED}FeatureModule.kt"
}

write_component_sources() {
    local base=$(pkg_path "commonMain")
    mkdir -p "$base"

    cat > "$base/${CAPITALIZED}Viewer.kt" <<-KT
package $PACKAGE

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun ${CAPITALIZED}Viewer(
    url: String,
    modifier: Modifier = Modifier,
)
KT

    # Android actual
    local android_dir=$(pkg_path "androidMain")
    mkdir -p "$android_dir"
    cat > "$android_dir/${CAPITALIZED}Viewer.android.kt" <<-KT
package $PACKAGE

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
actual fun ${CAPITALIZED}Viewer(
    url: String,
    modifier: Modifier,
) {
    AndroidView(
        modifier = modifier,
        factory = { context -> android.opengl.GLSurfaceView(context) },
    )
}
KT

    # iOS actual
    local ios_dir=$(pkg_path "iosMain")
    mkdir -p "$ios_dir"
    cat > "$ios_dir/${CAPITALIZED}Viewer.ios.kt" <<-KT
package $PACKAGE

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun ${CAPITALIZED}Viewer(
    url: String,
    modifier: Modifier,
) {
    // TODO: implement with UIKitView + SceneKit
}
KT

    # JVM actual
    local jvm_dir=$(pkg_path "jvmMain")
    mkdir -p "$jvm_dir"
    cat > "$jvm_dir/${CAPITALIZED}Viewer.jvm.kt" <<-KT
package $PACKAGE

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun ${CAPITALIZED}Viewer(
    url: String,
    modifier: Modifier,
) {
    // TODO: implement with Compose Canvas
}
KT

    echo "   - src/commonMain/.../${CAPITALIZED}Viewer.kt"
    echo "   - src/androidMain/.../${CAPITALIZED}Viewer.android.kt"
    echo "   - src/iosMain/.../${CAPITALIZED}Viewer.ios.kt"
    echo "   - src/jvmMain/.../${CAPITALIZED}Viewer.jvm.kt"
}

# ======================================================================
# 3. settings.gradle.kts
# ======================================================================
add_to_settings() {
    local settings="$ROOT/settings.gradle.kts"
    local line="include(\"$MODULE_ID\")"

    if grep -qF "$line" "$settings"; then
        echo "   -- already in settings.gradle.kts"
        return
    fi

    # Insert after the last existing include(...) line
    local last_include
    last_include=$(grep -n '^include(' "$settings" | tail -1 | cut -d: -f1)
    if [[ -n "$last_include" ]]; then
        sed -i '' "${last_include}a\\
${line}
" "$settings"
    else
        echo "" >> "$settings"
        echo "$line" >> "$settings"
    fi
    echo "   - added to settings.gradle.kts"
}

# ======================================================================
# Main
# ======================================================================
echo ""
echo "Creating $TYPE module: $LOWERCASE"
echo "  package: $PACKAGE"
echo "  path:    $TYPE/$LOWERCASE"
echo ""

write_build_gradle

mkdir -p "$MODULE_DIR/src/commonMain/kotlin/${PACKAGE//.//}"
mkdir -p "$MODULE_DIR/src/androidMain/kotlin/${PACKAGE//.//}"

case "$TYPE" in
    core)    write_core_sources ;;
    domain)  write_domain_sources ;;
    data)    write_data_sources ;;
    feature) write_feature_sources ;;
    component) write_component_sources ;;
esac

add_to_settings

echo ""
echo "Done. Module :$TYPE:$LOWERCASE created at $TYPE/$LOWERCASE/"
echo ""
echo "Next steps:"
case "$TYPE" in
    domain)
        echo "  1. Define business models in $TYPE/$LOWERCASE/src/commonMain/.../model/"
        echo "  2. Add repository methods in $TYPE/$LOWERCASE/src/commonMain/.../repository/"
        echo "  3. Implement use cases in $TYPE/$LOWERCASE/src/commonMain/.../usecase/"
        echo "  4. Create matching :data:$LOWERCASE module"
        echo "  5. Run ./gradlew :$TYPE:$LOWERCASE:compileKotlinJvm"
        ;;
    data)
        echo "  1. Implement API client in $TYPE/$LOWERCASE/src/commonMain/.../remote/"
        echo "  2. Add mappers in $TYPE/$LOWERCASE/src/commonMain/.../mapper/"
        echo "  3. Complete repository impl in $TYPE/$LOWERCASE/src/commonMain/.../repository/"
        echo "  4. Register ${CAPITALIZED}DataModule in app's DI graph"
        echo "  5. Run ./gradlew :$TYPE:$LOWERCASE:compileKotlinJvm"
        ;;
    feature)
        echo "  1. Build UI in $TYPE/$LOWERCASE/src/commonMain/.../presentation/${CAPITALIZED}Screen.kt"
        echo "  2. Add business logic in ${CAPITALIZED}ViewModel.kt"
        echo "  3. Register ${CAPITALIZED}FeatureModule in app's DI graph"
        echo "  4. Add Route to NavGraph"
        echo "  5. Run ./gradlew :$TYPE:$LOWERCASE:compileKotlinJvm"
        ;;
    core)
        echo "  1. Add infrastructure code in $TYPE/$LOWERCASE/src/commonMain/kotlin/"
        echo "  2. Update build.gradle.kts if extra deps are needed"
        echo "  3. Run ./gradlew :$TYPE:$LOWERCASE:compileKotlinJvm"
        ;;
    component)
        echo "  1. Implement platform renderers in androidMain/, iosMain/, jvmMain/"
        echo "  2. Run ./gradlew :$TYPE:$LOWERCASE:compileKotlinJvm :$TYPE:$LOWERCASE:compileKotlinIosArm64"
        ;;
esac
echo ""
