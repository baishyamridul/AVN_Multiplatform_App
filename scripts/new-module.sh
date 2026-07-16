#!/bin/bash
set -e

ROOT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
NAMESPACE="tech.sumato.kmptemplate"
LOWERCASE_NAME=""
MODULE_TYPE=""
CAPITALIZED_NAME=""

die() { echo "❌ $1" >&2; exit 1; }

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

to_package_segment() {
  echo "$1" | tr '-' '_'
}

prompt() {
  echo ""
  echo "=== New KMP Module Scaffold ==="
  echo ""

  # Module type
  echo "Module types:"
  echo "  1) core    — Infrastructure (network, database, navigation, etc.)"
  echo "  2) domain  — Business models, repository interfaces, use cases"
  echo "  3) data    — Repository impl, DTOs, API clients, mappers"
  echo "  4) feature — Screen, ViewModel, State, Event, Effect, Route"
  echo ""
  read -rp "Select type [1-4]: " type_choice
  case "$type_choice" in
    1) MODULE_TYPE="core" ;;
    2) MODULE_TYPE="domain" ;;
    3) MODULE_TYPE="data" ;;
    4) MODULE_TYPE="feature" ;;
    *) die "Invalid selection" ;;
  esac

  read -rp "Module name (kebab-case, e.g. farmer-registration): " RAW_NAME
  [[ -z "$RAW_NAME" ]] && die "Name cannot be empty"
  LOWERCASE_NAME=$(echo "$RAW_NAME" | tr '[:upper:]' '[:lower:]')
  CAPITALIZED_NAME=$(to_upper_camel "$LOWERCASE_NAME")
  PACKAGE_SEGMENT=$(to_package_segment "$LOWERCASE_NAME")
}

create_settings_include() {
  local inc_path=":$MODULE_TYPE:$LOWERCASE_NAME"
  if grep -q "include(\"$inc_path\")" "$ROOT_DIR/settings.gradle.kts"; then
    echo "   ⏭️  include already exists in settings.gradle.kts"
    return
  fi
  echo "" >> "$ROOT_DIR/settings.gradle.kts"
  echo "include(\"$inc_path\")" >> "$ROOT_DIR/settings.gradle.kts"
  echo "   ✅ Added include(\"$inc_path\") to settings.gradle.kts"
}

create_build_gradle() {
  local dir="$1/$LOWERCASE_NAME"
  mkdir -p "$dir"
  local file="$dir/build.gradle.kts"
  local base_name=""
  local namespace_line=""

  case "$MODULE_TYPE" in
    core)
      base_name="Core$(to_upper_camel "$LOWERCASE_NAME")"
      namespace_line="        namespace = \"$NAMESPACE.core.$PACKAGE_SEGMENT\""
      ;;
    domain)
      base_name="Domain$(to_upper_camel "$LOWERCASE_NAME")"
      namespace_line="        namespace = \"$NAMESPACE.domain.$PACKAGE_SEGMENT\""
      ;;
    data)
      base_name="Data$(to_upper_camel "$LOWERCASE_NAME")"
      namespace_line="        namespace = \"$NAMESPACE.data.$PACKAGE_SEGMENT\""
      ;;
    feature)
      base_name="Feature$(to_upper_camel "$LOWERCASE_NAME")"
      namespace_line="        namespace = \"$NAMESPACE.feature.$PACKAGE_SEGMENT\""
      ;;
  esac

  local plugins_block
  local deps_block=""

  case "$MODULE_TYPE" in
    core)
      plugins_block=$(cat <<-PLUGINS
	    alias(libs.plugins.kotlinMultiplatform)
	    alias(libs.plugins.androidMultiplatformLibrary)
PLUGINS
)
      deps_block=$(cat <<-DEPS
	        commonMain.dependencies {
	            implementation(libs.kotlinx.coroutines.core)
	        }
DEPS
)
      ;;
    domain)
      plugins_block=$(cat <<-PLUGINS
	    alias(libs.plugins.kotlinMultiplatform)
	    alias(libs.plugins.androidMultiplatformLibrary)
PLUGINS
)
      deps_block=$(cat <<-DEPS
	        commonMain.dependencies {
	            implementation(projects.core.common)
	            implementation(libs.kotlinx.coroutines.core)
	        }
DEPS
)
      ;;
    data)
      plugins_block=$(cat <<-PLUGINS
	    alias(libs.plugins.kotlinMultiplatform)
	    alias(libs.plugins.androidMultiplatformLibrary)
	    alias(libs.plugins.kotlinxSerialization)
PLUGINS
)
      deps_block=$(cat <<-DEPS
	        commonMain.dependencies {
	            implementation(projects.core.common)
	            implementation(projects.core.network)
	            implementation(projects.domain.$LOWERCASE_NAME)
	            implementation(libs.kotlinx.coroutines.core)
	            implementation(libs.kotlinx.serialization.json)
	            implementation(libs.koin.core)
	            implementation(libs.ktor.client.core)
	        }
DEPS
)
      ;;
    feature)
      plugins_block=$(cat <<-PLUGINS
	    alias(libs.plugins.kotlinMultiplatform)
	    alias(libs.plugins.androidMultiplatformLibrary)
	    alias(libs.plugins.composeMultiplatform)
	    alias(libs.plugins.composeCompiler)
PLUGINS
)
      deps_block=$(cat <<-DEPS
	        commonMain.dependencies {
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
	        }
DEPS
)
      ;;
  esac

  cat > "$file" <<-BUILD
	import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
	import org.jetbrains.kotlin.gradle.dsl.JvmTarget

	plugins {
	$plugins_block
	}

	kotlin {
	    listOf(
	        iosArm64(),
	        iosSimulatorArm64()
	    ).forEach { iosTarget ->
	        iosTarget.binaries.framework {
	            baseName = "$base_name"
	            isStatic = true
	        }
	    }

	    jvm()
	    js { browser() }

	    @OptIn(ExperimentalWasmDsl::class)
	    wasmJs { browser() }

	    androidLibrary {
	$namespace_line
	        compileSdk = libs.versions.android.compileSdk.get().toInt()
	        minSdk = libs.versions.android.minSdk.get().toInt()
	        compilerOptions { jvmTarget = JvmTarget.JVM_11 }
	    }

	    sourceSets {
	$deps_block
	    }
	}
BUILD
  echo "   ✅ Created $file"
}

create_domain_sources() {
  local pkg_dir="$1/src/commonMain/kotlin/$(echo "$NAMESPACE.domain.$PACKAGE_SEGMENT" | tr '.' '/')"

  mkdir -p "$pkg_dir/model"
  mkdir -p "$pkg_dir/repository"
  mkdir -p "$pkg_dir/usecase"

  # Model
  cat > "$pkg_dir/model/${CAPITALIZED_NAME}.kt" <<-KT
	package $NAMESPACE.domain.$PACKAGE_SEGMENT.model

	data class $CAPITALIZED_NAME(
	    val id: String,
	)
KT

  # Repository interface
  cat > "$pkg_dir/repository/${CAPITALIZED_NAME}Repository.kt" <<-KT
	package $NAMESPACE.domain.$PACKAGE_SEGMENT.repository

	import $NAMESPACE.domain.$PACKAGE_SEGMENT.model.$CAPITALIZED_NAME

	interface ${CAPITALIZED_NAME}Repository {
	    suspend fun get(): $CAPITALIZED_NAME
	}
KT

  # Use case
  cat > "$pkg_dir/usecase/Get${CAPITALIZED_NAME}UseCase.kt" <<-KT
	package $NAMESPACE.domain.$PACKAGE_SEGMENT.usecase

	import $NAMESPACE.domain.$PACKAGE_SEGMENT.repository.${CAPITALIZED_NAME}Repository

	class Get${CAPITALIZED_NAME}UseCase(
	    private val repository: ${CAPITALIZED_NAME}Repository,
	) {
	    suspend operator fun invoke() = repository.get()
	}
KT

  echo "   ✅ Created domain sources (model, repository, usecase)"
}

create_data_sources() {
  local pkg_dir="$1/src/commonMain/kotlin/$(echo "$NAMESPACE.data.$PACKAGE_SEGMENT" | tr '.' '/')"

  mkdir -p "$pkg_dir/remote"
  mkdir -p "$pkg_dir/mapper"
  mkdir -p "$pkg_dir/repository"
  mkdir -p "$pkg_dir/di"

  # DTO
  cat > "$pkg_dir/remote/${CAPITALIZED_NAME}Dto.kt" <<-KT
	package $NAMESPACE.data.$PACKAGE_SEGMENT.remote

	import kotlinx.serialization.Serializable

	@Serializable
	data class ${CAPITALIZED_NAME}Dto(
	    val id: String,
	)
KT

  # API
  cat > "$pkg_dir/remote/${CAPITALIZED_NAME}Api.kt" <<-KT
	package $NAMESPACE.data.$PACKAGE_SEGMENT.remote

	import io.ktor.client.HttpClient

	class ${CAPITALIZED_NAME}Api(
	    private val httpClient: HttpClient,
	)
KT

  # Mapper
  cat > "$pkg_dir/mapper/${CAPITALIZED_NAME}Mapper.kt" <<-KT
	package $NAMESPACE.data.$PACKAGE_SEGMENT.mapper

	import $NAMESPACE.data.$PACKAGE_SEGMENT.remote.${CAPITALIZED_NAME}Dto
	import $NAMESPACE.domain.$PACKAGE_SEGMENT.model.$CAPITALIZED_NAME

	class ${CAPITALIZED_NAME}Mapper {
	    fun toDomain(dto: ${CAPITALIZED_NAME}Dto): $CAPITALIZED_NAME {
	        return $CAPITALIZED_NAME(
	            id = dto.id,
	        )
	    }
	}
KT

  # Repository impl
  cat > "$pkg_dir/repository/${CAPITALIZED_NAME}RepositoryImpl.kt" <<-KT
	package $NAMESPACE.data.$PACKAGE_SEGMENT.repository

	import $NAMESPACE.data.$PACKAGE_SEGMENT.remote.${CAPITALIZED_NAME}Api
	import $NAMESPACE.data.$PACKAGE_SEGMENT.mapper.${CAPITALIZED_NAME}Mapper
	import $NAMESPACE.domain.$PACKAGE_SEGMENT.model.$CAPITALIZED_NAME
	import $NAMESPACE.domain.$PACKAGE_SEGMENT.repository.${CAPITALIZED_NAME}Repository

	class ${CAPITALIZED_NAME}RepositoryImpl(
	    private val api: ${CAPITALIZED_NAME}Api,
	    private val mapper: ${CAPITALIZED_NAME}Mapper,
	) : ${CAPITALIZED_NAME}Repository {

	    override suspend fun get(): $CAPITALIZED_NAME {
	        TODO("implement")
	    }
	}
KT

  # DI module
  cat > "$pkg_dir/di/${CAPITALIZED_NAME}DataModule.kt" <<-KT
	package $NAMESPACE.data.$PACKAGE_SEGMENT.di

	import org.koin.core.module.dsl.bind
	import org.koin.core.module.dsl.singleOf
	import org.koin.dsl.module
	import $NAMESPACE.data.$PACKAGE_SEGMENT.mapper.${CAPITALIZED_NAME}Mapper
	import $NAMESPACE.data.$PACKAGE_SEGMENT.remote.${CAPITALIZED_NAME}Api
	import $NAMESPACE.data.$PACKAGE_SEGMENT.repository.${CAPITALIZED_NAME}RepositoryImpl
	import $NAMESPACE.domain.$PACKAGE_SEGMENT.repository.${CAPITALIZED_NAME}Repository

	val ${CAPITALIZED_NAME}DataModule = module {
	    singleOf(::${CAPITALIZED_NAME}Api)
	    singleOf(::${CAPITALIZED_NAME}Mapper)
	    singleOf(::${CAPITALIZED_NAME}RepositoryImpl) { bind<${CAPITALIZED_NAME}Repository>() }
	}
KT

  echo "   ✅ Created data sources (remote, mapper, repository, di)"
}

create_feature_sources() {
  local pkg_dir="$1/src/commonMain/kotlin/$(echo "$NAMESPACE.feature.$PACKAGE_SEGMENT" | tr '.' '/')"

  mkdir -p "$pkg_dir/presentation"
  mkdir -p "$pkg_dir/di"

  # State
  cat > "$pkg_dir/presentation/${CAPITALIZED_NAME}State.kt" <<-KT
	package $NAMESPACE.feature.$PACKAGE_SEGMENT.presentation

	sealed interface ${CAPITALIZED_NAME}State {
	    data object Loading : ${CAPITALIZED_NAME}State
	    data object Idle : ${CAPITALIZED_NAME}State
	    data class Error(val message: String) : ${CAPITALIZED_NAME}State
	}
KT

  # Event
  cat > "$pkg_dir/presentation/${CAPITALIZED_NAME}Event.kt" <<-KT
	package $NAMESPACE.feature.$PACKAGE_SEGMENT.presentation

	sealed interface ${CAPITALIZED_NAME}Event
KT

  # Effect
  cat > "$pkg_dir/presentation/${CAPITALIZED_NAME}Effect.kt" <<-KT
	package $NAMESPACE.feature.$PACKAGE_SEGMENT.presentation

	sealed interface ${CAPITALIZED_NAME}Effect {
	    data class ShowSnackbar(val message: String) : ${CAPITALIZED_NAME}Effect
	}
KT

  # ViewModel
  cat > "$pkg_dir/presentation/${CAPITALIZED_NAME}ViewModel.kt" <<-KT
	package $NAMESPACE.feature.$PACKAGE_SEGMENT.presentation

	import androidx.lifecycle.ViewModel
	import kotlinx.coroutines.channels.Channel
	import kotlinx.coroutines.flow.Flow
	import kotlinx.coroutines.flow.MutableStateFlow
	import kotlinx.coroutines.flow.StateFlow
	import kotlinx.coroutines.flow.asStateFlow
	import kotlinx.coroutines.flow.receiveAsFlow
	import $NAMESPACE.core.navigation.MviViewModel

	class ${CAPITALIZED_NAME}ViewModel : ViewModel(), MviViewModel<${CAPITALIZED_NAME}State, ${CAPITALIZED_NAME}Effect> {

	    private val _state = MutableStateFlow<${CAPITALIZED_NAME}State>(${CAPITALIZED_NAME}State.Loading)
	    override val state: StateFlow<${CAPITALIZED_NAME}State> = _state.asStateFlow()

	    private val _effects = Channel<${CAPITALIZED_NAME}Effect>(Channel.BUFFERED)
	    override val effects: Flow<${CAPITALIZED_NAME}Effect> = _effects.receiveAsFlow()

	    fun onEvent(event: ${CAPITALIZED_NAME}Event) {
	        when (event) { }
	    }
	}
KT

  # Screen
  cat > "$pkg_dir/presentation/${CAPITALIZED_NAME}Screen.kt" <<-KT
	package $NAMESPACE.feature.$PACKAGE_SEGMENT.presentation

	import androidx.compose.foundation.layout.Box
	import androidx.compose.foundation.layout.fillMaxSize
	import androidx.compose.material3.Text
	import androidx.compose.runtime.Composable
	import androidx.compose.ui.Alignment
	import androidx.compose.ui.Modifier

	@Composable
	fun ${CAPITALIZED_NAME}Screen(
	    state: ${CAPITALIZED_NAME}State,
	    onEvent: (${CAPITALIZED_NAME}Event) -> Unit,
	) {
	    Box(
	        modifier = Modifier.fillMaxSize(),
	        contentAlignment = Alignment.Center,
	    ) {
	        Text("${CAPITALIZED_NAME}")
	    }
	}
KT

  # Route
  cat > "$pkg_dir/presentation/${CAPITALIZED_NAME}Route.kt" <<-KT
	package $NAMESPACE.feature.$PACKAGE_SEGMENT.presentation

	import androidx.compose.runtime.Composable
	import $NAMESPACE.core.navigation.BaseRoute

	@Composable
	fun ${CAPITALIZED_NAME}Route(
	    onShowSnackbar: (String) -> Unit = {},
	) {
	    BaseRoute<${CAPITALIZED_NAME}ViewModel, ${CAPITALIZED_NAME}State, ${CAPITALIZED_NAME}Effect>(
	        onEffect = { effect ->
	            when (effect) {
	                is ${CAPITALIZED_NAME}Effect.ShowSnackbar -> onShowSnackbar(effect.message)
	            }
	        },
	        content = { state ->
	            ${CAPITALIZED_NAME}Screen(
	                state = state,
	                onEvent = ::onEvent,
	            )
	        },
	    )
	}
KT

  # DI module
  cat > "$pkg_dir/di/${CAPITALIZED_NAME}FeatureModule.kt" <<-KT
	package $NAMESPACE.feature.$PACKAGE_SEGMENT.di

	import org.koin.core.module.dsl.viewModelOf
	import org.koin.dsl.module
	import $NAMESPACE.feature.$PACKAGE_SEGMENT.presentation.${CAPITALIZED_NAME}ViewModel

	val ${CAPITALIZED_NAME}FeatureModule = module {
	    viewModelOf(::${CAPITALIZED_NAME}ViewModel)
	}
KT

  echo "   ✅ Created feature sources (presentation/, di/)"
}

create_core_sources() {
  local pkg_dir="$1/src/commonMain/kotlin/$(echo "$NAMESPACE.core.$PACKAGE_SEGMENT" | tr '.' '/')"
  mkdir -p "$pkg_dir"

  cat > "$pkg_dir/Placeholder.kt" <<-KT
	package $NAMESPACE.core.$PACKAGE_SEGMENT

	object Placeholder
KT
  echo "   ✅ Created core source placeholder"
}

# --- Main ---
prompt

MODULE_DIR="$ROOT_DIR/$MODULE_TYPE/$LOWERCASE_NAME"
if [[ -d "$MODULE_DIR" ]]; then
  die "Module directory already exists: $MODULE_DIR"
fi

echo ""
echo "Scaffolding $MODULE_TYPE:$LOWERCASE_NAME ..."
echo ""

create_build_gradle "$ROOT_DIR/$MODULE_TYPE"

case "$MODULE_TYPE" in
  core)   create_core_sources "$MODULE_DIR" ;;
  domain) create_domain_sources "$MODULE_DIR" ;;
  data)   create_data_sources "$MODULE_DIR" ;;
  feature) create_feature_sources "$MODULE_DIR" ;;
esac

create_settings_include

echo ""
echo "✅ Module $MODULE_TYPE:$LOWERCASE_NAME created successfully!"
echo ""
echo "Next steps:"
case "$MODULE_TYPE" in
  domain)
    echo "  1. Define your business models in domain/$LOWERCASE_NAME/model/"
    echo "  2. Add repository interface in domain/$LOWERCASE_NAME/repository/"
    echo "  3. Add use cases in domain/$LOWERCASE_NAME/usecase/"
    echo "  4. Create a matching :data:$LOWERCASE_NAME module"
    ;;
  data)
    echo "  1. Implement API client in data/$LOWERCASE_NAME/remote/"
    echo "  2. Add mappers in data/$LOWERCASE_NAME/mapper/"
    echo "  3. Complete repository impl in data/$LOWERCASE_NAME/repository/"
    echo "  4. Register ${CAPITALIZED_NAME}DataModule in shared/App.kt"
    echo "  5. Add dependency in shared/build.gradle.kts if needed"
    ;;
  feature)
    echo "  1. Implement UI in feature/$LOWERCASE_NAME/presentation/Screen.kt"
    echo "  2. Add business logic in feature/$LOWERCASE_NAME/presentation/ViewModel.kt"
    echo "  3. Register ${CAPITALIZED_NAME}FeatureModule in shared/App.kt"
    echo "  4. Add Route to NavGraph in shared/App.kt"
    echo "  5. Add dependency in shared/build.gradle.kts if needed"
    ;;
  core)
    echo "  1. Add your infrastructure code in core/$LOWERCASE_NAME/"
    echo "  2. Update build.gradle.kts with additional dependencies if needed"
    ;;
esac
echo ""
