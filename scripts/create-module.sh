#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
NAMESPACE="tech.sumato.avn.mp"
SETTINGS="$ROOT/settings.gradle.kts"

die() { echo "Error: $*" >&2; exit 1; }

is_valid_name() {
    [[ "$1" =~ ^[A-Za-z][A-Za-z0-9_-]*$ ]]
}

flatten_name() { echo "${1//[-_]/}" | tr '[:upper:]' '[:lower:]'; }

confirm() {
    local message="$1" default="$2" answer
    if [[ "$default" == "Y" ]]; then
        read -rp "${message} [Y/n]: " answer
        [[ -z "$answer" || "$answer" == [Yy]* ]]
    else
        read -rp "${message} [y/N]: " answer
        [[ "$answer" == [Yy]* ]]
    fi
}

to_pascal() {
    python3 -c "import sys; print(''.join(w.capitalize() for w in sys.argv[1].split()))" "$1"
}

usage_banner() {
    cat <<'EOF'
============================================================
  AVN KMP Module Creator
  Creates a new Gradle module following the .ai architecture.
============================================================
Layers:
  1) domain      Business models, repository interfaces, use cases
  2) data        DTOs, API clients, mappers, repository impl, DI
  3) feature     Screen, ViewModel, State, Event, Effect, Route, DI
  4) component   Reusable KMP composable (expect/actual)
  5) core        Infrastructure (network, datastore, navigation...)
  6) other       Custom layer (falls back to the core template)

Module names stay camelCase (e.g. districtDashboard, schoolDashboard).
Package names use snake_case (e.g. tech.sumato.avn.mp.feature.school_dashboard).
Targets: Android, iOS (arm64 + simulator), Desktop (JVM).
EOF
}

# ======================================================================
# 1. Layer menu
# ======================================================================
usage_banner
echo ""
while true; do
    read -rp "Choose a layer [1-6]: " choice
    case "$choice" in
        1) TYPE="domain"; break ;;
        2) TYPE="data"; break ;;
        3) TYPE="feature"; break ;;
        4) TYPE="component"; break ;;
        5) TYPE="core"; break ;;
        6)
            read -rp "Custom layer name (e.g. sync): " CUSTOM_LAYER
            if is_valid_name "$CUSTOM_LAYER"; then
                TYPE="$CUSTOM_LAYER"
                break
            fi
            echo "Invalid layer name."
            ;;
        *) echo "Invalid choice. Pick a number 1-6." ;;
    esac
done

# ======================================================================
# 2. Module name (camelCase)
# ======================================================================
while true; do
    read -rp "Module name (camelCase, e.g. schoolDashboard): " RAW_NAME
    if is_valid_name "$RAW_NAME"; then
        break
    fi
    echo "Invalid name. Use letters, digits, '-' or '_' (must start with a letter)."
done

# Normalize to segments, then derive camel/snake/pascal forms.
SEGMENTS="$(python3 - "$RAW_NAME" <<'PY'
import re, sys
name = sys.argv[1]
s = re.sub(r"[-_]", " ", name)
parts = [p for p in re.split(r"(?<=[a-z0-9])(?=[A-Z])|\s+", s) if p]
print(" ".join(p.lower() for p in parts))
PY
)"
SNAKE="${SEGMENTS// /_}"
PASCAL="$(to_pascal "$SEGMENTS")"
CAMEL="$(python3 -c "s='$PASCAL'; print(s[0].lower() + s[1:])")"

case "$TYPE" in
    domain)   LAYER_PASCAL="Domain" ;;
    data)     LAYER_PASCAL="Data" ;;
    feature)  LAYER_PASCAL="Feature" ;;
    component) LAYER_PASCAL="Component" ;;
    core)     LAYER_PASCAL="Core" ;;
    *)        LAYER_PASCAL="$(to_pascal "$TYPE")" ;;
esac

MODULE_ID=":$TYPE:$CAMEL"
MODULE_DIR="$ROOT/$TYPE/$CAMEL"
PACKAGE="$NAMESPACE.$TYPE.$SNAKE"
DOMAIN_PACKAGE="$NAMESPACE.domain.$SNAKE"
BASENAME="${LAYER_PASCAL}${PASCAL}"

[[ -d "$MODULE_DIR" ]] && die "Module directory already exists: $MODULE_DIR"

# Guard against Gradle type-safe project accessor collisions
# (e.g. :feature:school-dashboard and :feature:schoolDashboard both
# generate projects.feature.schoolDashboard).
if [[ -f "$SETTINGS" ]]; then
    flat_camel="$(flatten_name "$CAMEL")"
    for existing in $(grep -oE "^include\(\":$TYPE:[^\"]+" "$SETTINGS" | sed -E "s/^include\(\":$TYPE://" || true); do
        if [[ "$(flatten_name "$existing")" == "$flat_camel" ]]; then
            die "A module ':$TYPE:$existing' already exists and collides with '$MODULE_ID' (both map to the same Gradle project accessor). Pick a different name."
        fi
    done
fi

echo ""
echo "Creating $TYPE module: $MODULE_ID"
echo "  package:  $PACKAGE"
echo "  path:     $TYPE/$CAMEL"
echo "  iOS name: $BASENAME"
echo ""

# ======================================================================
# 3. Cross-layer pairing
# ======================================================================
CREATE_DOMAIN=false
CREATE_DATA=false

case "$TYPE" in
    data)
        if confirm "Create matching domain module domain/$CAMEL too?" "Y"; then
            CREATE_DOMAIN=true
        fi
        ;;
    domain)
        if confirm "Create matching data module data/$CAMEL too?" "N"; then
            CREATE_DATA=true
        fi
        ;;
    feature)
        if confirm "Create matching domain/$CAMEL and data/$CAMEL modules too?" "Y"; then
            CREATE_DOMAIN=true
            CREATE_DATA=true
        fi
        ;;
esac

DOMAIN_PRESENT=false
if [[ "$TYPE" == "domain" || "$CREATE_DOMAIN" == true ]]; then
    DOMAIN_PRESENT=true
fi

# ======================================================================
# settings.gradle.kts
# ======================================================================
add_to_settings() {
    local id="$1" layer="$2"
    local line="include(\"$id\")"

    if grep -qF "$line" "$SETTINGS"; then
        echo "   -- $id already registered in settings.gradle.kts"
        return
    fi

    local anchor
    anchor="$(grep -n "^include(\":$layer:" "$SETTINGS" | tail -1 | cut -d: -f1)"
    if [[ -z "$anchor" ]]; then
        anchor="$(grep -n '^include(' "$SETTINGS" | tail -1 | cut -d: -f1)"
    fi

    if [[ -n "$anchor" ]]; then
        sed -i '' "${anchor}a\\
${line}
" "$SETTINGS"
    else
        printf '\n%s\n' "$line" >> "$SETTINGS"
    fi
    echo "   - added $id to settings.gradle.kts"
}

# ======================================================================
# build.gradle.kts
# ======================================================================
write_build_gradle() {
    local dir="$1" type="$2" basename="$3" package="$4" domain_dep="$5"

    local plugins=""
    case "$type" in
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

    local deps=""
    case "$type" in
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
            ${domain_dep}implementation(projects.domain.${CAMEL})
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
            ${domain_dep}implementation(projects.domain.${CAMEL})
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.material.icons)
            implementation(libs.material.icons.extended)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.date.time)
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
        core)
            deps=$(cat <<-DEP
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
        }
DEP
)
            ;;
    esac

    mkdir -p "$dir"
    cat > "$dir/build.gradle.kts" <<-BUILD
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
            baseName = "$basename"
            isStatic = true
        }
    }

    jvm()
//    js { browser() }

//    @OptIn(ExperimentalWasmDsl::class)
//    wasmJs { browser() }

    androidLibrary {
        namespace = "$package"
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
# Source templates
# ======================================================================
# CUR_DIR / CUR_PACKAGE are set per module being created (a run may
# create paired domain + data + feature modules).
src_path() { echo "$CUR_DIR/src/$1/kotlin/${CUR_PACKAGE//.//}"; }

write_domain_sources() {
    local base
    base="$(src_path commonMain)"

    mkdir -p "$base/model" "$base/repository" "$base/usecase"

    cat > "$base/model/$PASCAL.kt" <<-KT
package $CUR_PACKAGE.model

data class $PASCAL(
    val id: String,
)
KT

    cat > "$base/repository/${PASCAL}Repository.kt" <<-KT
package $CUR_PACKAGE.repository

import $CUR_PACKAGE.model.$PASCAL

interface ${PASCAL}Repository {
    suspend fun get(): $PASCAL
}
KT

    cat > "$base/usecase/Get${PASCAL}UseCase.kt" <<-KT
package $CUR_PACKAGE.usecase

import $CUR_PACKAGE.model.$PASCAL
import $CUR_PACKAGE.repository.${PASCAL}Repository

class Get${PASCAL}UseCase(
    private val repository: ${PASCAL}Repository,
) {
    suspend operator fun invoke(): $PASCAL = repository.get()
}
KT

    echo "   - src/commonMain/.../model/$PASCAL.kt"
    echo "   - src/commonMain/.../repository/${PASCAL}Repository.kt"
    echo "   - src/commonMain/.../usecase/Get${PASCAL}UseCase.kt"
}

write_data_sources() {
    local base
    base="$(src_path commonMain)"

    mkdir -p "$base/remote" "$base/repository" "$base/di"

    cat > "$base/remote/${PASCAL}Dto.kt" <<-KT
package $CUR_PACKAGE.remote

import kotlinx.serialization.Serializable

@Serializable
data class ${PASCAL}Dto(
    val id: String,
)
KT

    cat > "$base/remote/${PASCAL}Api.kt" <<-KT
package $CUR_PACKAGE.remote

import io.ktor.client.HttpClient

class ${PASCAL}Api(
    private val httpClient: HttpClient,
)
KT

    if [[ "$DOMAIN_PRESENT" == true ]]; then
        mkdir -p "$base/mapper"
        cat > "$base/mapper/${PASCAL}Mapper.kt" <<-KT
package $CUR_PACKAGE.mapper

import $CUR_PACKAGE.remote.${PASCAL}Dto
import $DOMAIN_PACKAGE.model.$PASCAL

class ${PASCAL}Mapper {
    fun toDomain(dto: ${PASCAL}Dto): $PASCAL {
        return $PASCAL(
            id = dto.id,
        )
    }
}
KT

        cat > "$base/repository/${PASCAL}RepositoryImpl.kt" <<-KT
package $CUR_PACKAGE.repository

import $CUR_PACKAGE.mapper.${PASCAL}Mapper
import $CUR_PACKAGE.remote.${PASCAL}Api
import $DOMAIN_PACKAGE.model.$PASCAL
import $DOMAIN_PACKAGE.repository.${PASCAL}Repository

class ${PASCAL}RepositoryImpl(
    private val api: ${PASCAL}Api,
    private val mapper: ${PASCAL}Mapper,
) : ${PASCAL}Repository {

    override suspend fun get(): $PASCAL {
        TODO("implement")
    }
}
KT

        cat > "$base/di/${PASCAL}DataModule.kt" <<-KT
package $CUR_PACKAGE.di

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import $CUR_PACKAGE.mapper.${PASCAL}Mapper
import $CUR_PACKAGE.remote.${PASCAL}Api
import $CUR_PACKAGE.repository.${PASCAL}RepositoryImpl
import $DOMAIN_PACKAGE.repository.${PASCAL}Repository

val ${PASCAL}DataModule = module {
    singleOf(::${PASCAL}Api)
    singleOf(::${PASCAL}Mapper)
    singleOf(::${PASCAL}RepositoryImpl) { bind<${PASCAL}Repository>() }
}
KT

        echo "   - src/commonMain/.../remote/${PASCAL}Dto.kt"
        echo "   - src/commonMain/.../remote/${PASCAL}Api.kt"
        echo "   - src/commonMain/.../mapper/${PASCAL}Mapper.kt"
        echo "   - src/commonMain/.../repository/${PASCAL}RepositoryImpl.kt"
        echo "   - src/commonMain/.../di/${PASCAL}DataModule.kt"
    else
        cat > "$base/repository/${PASCAL}RepositoryImpl.kt" <<-KT
package $CUR_PACKAGE.repository

import $CUR_PACKAGE.remote.${PASCAL}Api
import $CUR_PACKAGE.remote.${PASCAL}Dto

class ${PASCAL}RepositoryImpl(
    private val api: ${PASCAL}Api,
) {
    suspend fun get(): ${PASCAL}Dto {
        TODO("implement")
    }
}
KT

        cat > "$base/di/${PASCAL}DataModule.kt" <<-KT
package $CUR_PACKAGE.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import $CUR_PACKAGE.remote.${PASCAL}Api
import $CUR_PACKAGE.repository.${PASCAL}RepositoryImpl

val ${PASCAL}DataModule = module {
    singleOf(::${PASCAL}Api)
    singleOf(::${PASCAL}RepositoryImpl)
}
KT

        echo "   - src/commonMain/.../remote/${PASCAL}Dto.kt"
        echo "   - src/commonMain/.../remote/${PASCAL}Api.kt"
        echo "   - src/commonMain/.../repository/${PASCAL}RepositoryImpl.kt"
        echo "   - src/commonMain/.../di/${PASCAL}DataModule.kt"
    fi
}

write_feature_sources() {
    local base
    base="$(src_path commonMain)"

    mkdir -p "$base/presentation/state" "$base/presentation/event" "$base/presentation/effect" "$base/di"

    cat > "$base/presentation/state/${PASCAL}State.kt" <<-KT
package $CUR_PACKAGE.presentation.state

sealed interface ${PASCAL}State {
    data object Loading : ${PASCAL}State
    data object Idle : ${PASCAL}State
    data class Error(val message: String) : ${PASCAL}State
}
KT

    cat > "$base/presentation/event/${PASCAL}Event.kt" <<-KT
package $CUR_PACKAGE.presentation.event

sealed interface ${PASCAL}Event
KT

    cat > "$base/presentation/effect/${PASCAL}Effect.kt" <<-KT
package $CUR_PACKAGE.presentation.effect

sealed interface ${PASCAL}Effect {
    data class ShowSnackbar(val message: String) : ${PASCAL}Effect
}
KT

    cat > "$base/presentation/${PASCAL}ViewModel.kt" <<-KT
package $CUR_PACKAGE.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import tech.sumato.avn.mp.core.navigation.MviViewModel
import $CUR_PACKAGE.presentation.effect.${PASCAL}Effect
import $CUR_PACKAGE.presentation.event.${PASCAL}Event
import $CUR_PACKAGE.presentation.state.${PASCAL}State

class ${PASCAL}ViewModel : ViewModel(), MviViewModel<${PASCAL}State, ${PASCAL}Effect> {

    private val _state = MutableStateFlow<${PASCAL}State>(${PASCAL}State.Loading)
    override val state: StateFlow<${PASCAL}State> = _state.asStateFlow()

    private val _effects = Channel<${PASCAL}Effect>(Channel.BUFFERED)
    override val effects: Flow<${PASCAL}Effect> = _effects.receiveAsFlow()

    fun onEvent(event: ${PASCAL}Event) {
    }
}
KT

    cat > "$base/presentation/${PASCAL}Screen.kt" <<-KT
package $CUR_PACKAGE.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import $CUR_PACKAGE.presentation.event.${PASCAL}Event
import $CUR_PACKAGE.presentation.state.${PASCAL}State

@Composable
fun ${PASCAL}Screen(
    state: ${PASCAL}State,
    onEvent: (${PASCAL}Event) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text("$PASCAL")
    }
}
KT

    cat > "$base/presentation/${PASCAL}Route.kt" <<-KT
package $CUR_PACKAGE.presentation

import androidx.compose.runtime.Composable
import tech.sumato.avn.mp.core.navigation.BaseRoute
import $CUR_PACKAGE.presentation.effect.${PASCAL}Effect
import $CUR_PACKAGE.presentation.state.${PASCAL}State

@Composable
fun ${PASCAL}Route(
    onShowSnackbar: (String) -> Unit = {},
) {
    BaseRoute<${PASCAL}ViewModel, ${PASCAL}State, ${PASCAL}Effect>(
        onEffect = { effect ->
            when (effect) {
                is ${PASCAL}Effect.ShowSnackbar -> onShowSnackbar(effect.message)
            }
        },
        content = { state ->
            ${PASCAL}Screen(
                state = state,
                onEvent = ::onEvent,
            )
        },
    )
}
KT

    cat > "$base/di/${PASCAL}FeatureModule.kt" <<-KT
package $CUR_PACKAGE.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import $CUR_PACKAGE.presentation.${PASCAL}ViewModel

val ${PASCAL}FeatureModule = module {
    viewModelOf(::${PASCAL}ViewModel)
}
KT

    echo "   - src/commonMain/.../presentation/state/${PASCAL}State.kt"
    echo "   - src/commonMain/.../presentation/event/${PASCAL}Event.kt"
    echo "   - src/commonMain/.../presentation/effect/${PASCAL}Effect.kt"
    echo "   - src/commonMain/.../presentation/${PASCAL}ViewModel.kt"
    echo "   - src/commonMain/.../presentation/${PASCAL}Screen.kt"
    echo "   - src/commonMain/.../presentation/${PASCAL}Route.kt"
    echo "   - src/commonMain/.../di/${PASCAL}FeatureModule.kt"
}

write_component_sources() {
    local base android_dir ios_dir jvm_dir
    base="$(src_path commonMain)"
    android_dir="$(src_path androidMain)"
    ios_dir="$(src_path iosMain)"
    jvm_dir="$(src_path jvmMain)"

    mkdir -p "$base" "$android_dir" "$ios_dir" "$jvm_dir"

    cat > "$base/${PASCAL}Viewer.kt" <<-KT
package $CUR_PACKAGE

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun ${PASCAL}Viewer(
    url: String,
    modifier: Modifier = Modifier,
)
KT

    cat > "$android_dir/${PASCAL}Viewer.android.kt" <<-KT
package $CUR_PACKAGE

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
actual fun ${PASCAL}Viewer(
    url: String,
    modifier: Modifier,
) {
    AndroidView(
        modifier = modifier,
        factory = { context -> android.opengl.GLSurfaceView(context) },
    )
}
KT

    cat > "$ios_dir/${PASCAL}Viewer.ios.kt" <<-KT
package $CUR_PACKAGE

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun ${PASCAL}Viewer(
    url: String,
    modifier: Modifier,
) {
    // TODO: implement with UIKitView + SceneKit
}
KT

    cat > "$jvm_dir/${PASCAL}Viewer.jvm.kt" <<-KT
package $CUR_PACKAGE

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun ${PASCAL}Viewer(
    url: String,
    modifier: Modifier,
) {
    // TODO: implement with Compose Canvas
}
KT

    echo "   - src/commonMain/.../${PASCAL}Viewer.kt"
    echo "   - src/androidMain/.../${PASCAL}Viewer.android.kt"
    echo "   - src/iosMain/.../${PASCAL}Viewer.ios.kt"
    echo "   - src/jvmMain/.../${PASCAL}Viewer.jvm.kt"
}

write_core_sources() {
    local base
    base="$(src_path commonMain)"
    mkdir -p "$base"

    cat > "$base/Placeholder.kt" <<-KT
package $CUR_PACKAGE

object Placeholder
KT

    echo "   - src/commonMain/.../Placeholder.kt"
}

# ======================================================================
# Module creation
# ======================================================================
create_domain() {
    CUR_DIR="$ROOT/domain/$CAMEL"
    CUR_PACKAGE="$NAMESPACE.domain.$SNAKE"
    echo ""
    echo "Creating domain module :domain:$CAMEL"
    write_build_gradle "$CUR_DIR" "domain" "Domain${PASCAL}" "$CUR_PACKAGE" ""
    write_domain_sources
    add_to_settings ":domain:$CAMEL" "domain"
}

create_data() {
    CUR_DIR="$ROOT/data/$CAMEL"
    CUR_PACKAGE="$NAMESPACE.data.$SNAKE"
    echo ""
    echo "Creating data module :data:$CAMEL"
    if [[ "$DOMAIN_PRESENT" == true ]]; then
        write_build_gradle "$CUR_DIR" "data" "Data${PASCAL}" "$CUR_PACKAGE" ""
    else
        write_build_gradle "$CUR_DIR" "data" "Data${PASCAL}" "$CUR_PACKAGE" "//"
    fi
    write_data_sources
    if [[ "$DOMAIN_PRESENT" != true ]]; then
        echo ""
        echo "  Note: no domain module was created. data/$CAMEL is standalone;"
        echo "  add a domain/$CAMEL module later to implement a repository interface."
    fi
    add_to_settings ":data:$CAMEL" "data"
}

create_feature() {
    CUR_DIR="$ROOT/feature/$CAMEL"
    CUR_PACKAGE="$NAMESPACE.feature.$SNAKE"
    echo ""
    echo "Creating feature module :feature:$CAMEL"
    if [[ "$CREATE_DOMAIN" == true ]]; then
        write_build_gradle "$CUR_DIR" "feature" "Feature${PASCAL}" "$CUR_PACKAGE" ""
    else
        write_build_gradle "$CUR_DIR" "feature" "Feature${PASCAL}" "$CUR_PACKAGE" "//"
    fi
    write_feature_sources
    add_to_settings ":feature:$CAMEL" "feature"
}

case "$TYPE" in
    domain)
        create_domain
        [[ "$CREATE_DATA" == true ]] && create_data
        ;;
    data)
        [[ "$CREATE_DOMAIN" == true ]] && create_domain
        create_data
        ;;
    feature)
        [[ "$CREATE_DOMAIN" == true ]] && create_domain
        [[ "$CREATE_DATA" == true ]] && create_data
        create_feature
        ;;
    component)
        CUR_DIR="$MODULE_DIR"
        CUR_PACKAGE="$PACKAGE"
        write_build_gradle "$CUR_DIR" "component" "$BASENAME" "$CUR_PACKAGE" ""
        write_component_sources
        add_to_settings "$MODULE_ID" "$TYPE"
        ;;
    *)
        CUR_DIR="$MODULE_DIR"
        CUR_PACKAGE="$PACKAGE"
        write_build_gradle "$CUR_DIR" "core" "$BASENAME" "$CUR_PACKAGE" ""
        write_core_sources
        add_to_settings "$MODULE_ID" "$TYPE"
        ;;
esac

# ======================================================================
# Summary
# ======================================================================
echo ""
echo "Done. Created:"
echo "  - $MODULE_DIR/"
echo ""
echo "Next steps:"
case "$TYPE" in
    domain|data|feature)
        echo "  1. Register the generated DI module(s) in shared/App.kt"
        echo "  2. Add the Route to the NavGraph in shared/App.kt (feature)"
        echo "  3. Implement API calls / repository / UI in the generated skeletons"
        ;;
    *)
        echo "  1. Add infrastructure / component code"
        ;;
esac
echo ""

if confirm "Run ./gradlew ${MODULE_ID}:compileKotlinJvm to verify?" "Y"; then
    (cd "$ROOT" && ./gradlew "${MODULE_ID}:compileKotlinJvm")
fi
