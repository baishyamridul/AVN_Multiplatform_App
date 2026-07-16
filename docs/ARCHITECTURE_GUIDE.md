# Architecture Guide — Adding Modules, Features & Network Operations

## Dependency Flow

```
Feature  ──→  Domain  ──→  Core
  │               │
  │               ├── Repository Interface
  │               ├── Business Models
  │               └── Use Cases
  │
  └── depends on Domain + DesignSystem only
  └── NEVER depends on Data

Data  ──→  Domain  ──→  Core
  │
  ├── DTOs, API Clients, DAOs
  ├── Repository Implementation
  ├── Mappers (DTO → Domain)
  └── DI Module

Core  ──→  (no project dependencies)
  ├── Infrastructure (Network, Database, Logger, etc.)
  └── Platform expect/actual

DesignSystem  ──→  (standalone)
  └── Theme, Components, Typography
```

| Allowed | Forbidden |
|---|---|
| Feature → Domain, Core, DesignSystem | Feature → Data |
| Data → Domain, Core | Feature → Feature |
| Domain → Core | Domain → Data |
| Core → (nothing in project) | Core → Domain, Core → Feature |

---

## Step 1: Decide What to Create

Ask the three questions from `general_instruction.md`:

| Question | If answer is... | Then create... |
|---|---|---|
| Which module? | Domain (business concept) | `domain/<concept>/` |
| Which module? | Data (implements domain) | `data/<concept>/` |
| Which module? | Feature (user workflow) | `feature/<name>/` |
| Which module? | Core (infrastructure) | `core/<name>/` |
| What kind? | Infrastructure | (in `core/`) |
| What kind? | Business | (in `domain/`) |
| What kind? | Persistence | (in `data/`) |
| What kind? | UI | (in `feature/` or `designsystem/`) |

**Good feature names** (per `feature_layer.md`): `login`, `dashboard`, `farmer-registration`, `survey-create`, `map`, `reports`

**Bad feature names** (these are domain concepts): `farmer`, `user`, `crop`

---

## Step 2: Register the Module

**`settings.gradle.kts`** — add the module path:

```kotlin
include(":domain:farmer")
include(":data:farmer")
include(":feature:farmer-registration")
```

---

## Step 3: Create `build.gradle.kts` for Each Module

### Domain module (pure business, no Compose)

```kotlin
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
}

kotlin {
    listOf(iosArm64(), iosSimulatorArm64()).forEach { iosTarget ->
        iosTarget.binaries.framework { baseName = "DomainFarmer"; isStatic = true }
    }
    jvm()
    js { browser() }
    @OptIn(ExperimentalWasmDsl::class) wasmJs { browser() }

    androidLibrary {
        namespace = "tech.sumato.kmptemplate.domain.farmer"
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
```

### Data module (DTOs, API, repository impl, mappers)

```kotlin
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.kotlinxSerialization)
}

kotlin {
    listOf(iosArm64(), iosSimulatorArm64()).forEach { iosTarget ->
        iosTarget.binaries.framework { baseName = "DataFarmer"; isStatic = true }
    }
    jvm()
    js { browser() }
    @OptIn(ExperimentalWasmDsl::class) wasmJs { browser() }

    androidLibrary {
        namespace = "tech.sumato.kmptemplate.data.farmer"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        compilerOptions { jvmTarget = JvmTarget.JVM_11 }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.domain.farmer)
            implementation(projects.core.network)
            implementation(projects.core.common)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.koin.core)
            implementation(libs.ktor.client.core)
        }
    }
}
```

### Feature module (Compose UI, ViewModel)

```kotlin
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    listOf(iosArm64(), iosSimulatorArm64()).forEach { iosTarget ->
        iosTarget.binaries.framework { baseName = "FeatureFarmerRegistration"; isStatic = true }
    }
    jvm()
    js { browser() }
    @OptIn(ExperimentalWasmDsl::class) wasmJs { browser() }

    androidLibrary {
        namespace = "tech.sumato.kmptemplate.feature.farmer.registration"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        compilerOptions { jvmTarget = JvmTarget.JVM_11 }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.domain.farmer)
            implementation(projects.designsystem)
            implementation(projects.core.common)

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
    }
}
```

---

## Step 4: Create Source Files by Layer

### Domain — `domain/<concept>/`

```
src/commonMain/kotlin/tech/sumato/kmptemplate/domain/<concept>/
  model/
    <ModelName>.kt
  repository/
    <Concept>Repository.kt
  usecase/
    Get<Something>UseCase.kt
    Save<Something>UseCase.kt
```

**Rules** (per `domain_layer.md`, `business_models.md`):
- Models are plain `data class` — no annotations, no serialization
- Repository interface returns domain models only — never DTOs or entities
- Use cases are single-responsibility classes with `operator fun invoke()`
- Constructor injection

**Example — model:**

```kotlin
package tech.sumato.kmptemplate.domain.farmer.model

data class Farmer(
    val id: String,
    val name: String,
    val phone: String,
    val village: String,
)
```

**Example — repository interface:**

```kotlin
package tech.sumato.kmptemplate.domain.farmer.repository

import tech.sumato.kmptemplate.domain.farmer.model.Farmer

interface FarmerRepository {
    suspend fun getFarmer(id: String): Farmer
    suspend fun saveFarmer(farmer: Farmer): Farmer
    suspend fun deleteFarmer(id: String)
}
```

**Example — use case:**

```kotlin
package tech.sumato.kmptemplate.domain.farmer.usecase

import tech.sumato.kmptemplate.domain.farmer.model.Farmer
import tech.sumato.kmptemplate.domain.farmer.repository.FarmerRepository

class SaveFarmerUseCase(
    private val farmerRepository: FarmerRepository,
) {
    suspend operator fun invoke(farmer: Farmer): Farmer {
        return farmerRepository.saveFarmer(farmer)
    }
}
```

### Data — `data/<concept>/`

```
src/commonMain/kotlin/tech/sumato/kmptemplate/data/<concept>/
  remote/
    <Concept>Api.kt
    <Concept>Dto.kt
  mapper/
    <Concept>Mapper.kt
  repository/
    <Concept>RepositoryImpl.kt
  di/
    <Concept>DataModule.kt
```

**Rules** (per `dto_rules.md`, `entity_rules.md`, `mapping_rules.md`, `repository_rules.md`):
- DTOs are `@Serializable`, live only in `data/`, never exposed outside
- Mapper converts `Dto → Domain` (never the reverse for UI flow)
- RepositoryImpl calls Api, uses Mapper, returns domain models
- DI module provides Impl bound to interface

**Example — DTO:**

```kotlin
package tech.sumato.kmptemplate.data.farmer.remote

import kotlinx.serialization.Serializable

@Serializable
data class FarmerRequestDto(
    val name: String,
    val phone: String,
    val village: String,
)

@Serializable
data class FarmerResponseDto(
    val id: String,
    val name: String,
    val phone: String,
    val village: String,
)
```

**Example — API client:**

```kotlin
package tech.sumato.kmptemplate.data.farmer.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class FarmerApi(
    private val httpClient: HttpClient,
) {
    suspend fun getFarmer(id: String): FarmerResponseDto {
        return httpClient.get("farmers/$id").body()
    }

    suspend fun saveFarmer(request: FarmerRequestDto): FarmerResponseDto {
        return httpClient.post("farmers") {
            setBody(request)
        }.body()
    }
}
```

**Example — mapper:**

```kotlin
package tech.sumato.kmptemplate.data.farmer.mapper

import tech.sumato.kmptemplate.data.farmer.remote.FarmerResponseDto
import tech.sumato.kmptemplate.domain.farmer.model.Farmer

class FarmerMapper {
    fun toDomain(dto: FarmerResponseDto): Farmer {
        return Farmer(
            id = dto.id,
            name = dto.name,
            phone = dto.phone,
            village = dto.village,
        )
    }
}
```

**Example — repository implementation:**

```kotlin
package tech.sumato.kmptemplate.data.farmer.repository

import tech.sumato.kmptemplate.data.farmer.mapper.FarmerMapper
import tech.sumato.kmptemplate.data.farmer.remote.FarmerApi
import tech.sumato.kmptemplate.data.farmer.remote.FarmerRequestDto
import tech.sumato.kmptemplate.domain.farmer.model.Farmer
import tech.sumato.kmptemplate.domain.farmer.repository.FarmerRepository

class FarmerRepositoryImpl(
    private val farmerApi: FarmerApi,
    private val farmerMapper: FarmerMapper,
) : FarmerRepository {

    override suspend fun getFarmer(id: String): Farmer {
        val response = farmerApi.getFarmer(id)
        return farmerMapper.toDomain(response)
    }

    override suspend fun saveFarmer(farmer: Farmer): Farmer {
        val request = FarmerRequestDto(
            name = farmer.name,
            phone = farmer.phone,
            village = farmer.village,
        )
        val response = farmerApi.saveFarmer(request)
        return farmerMapper.toDomain(response)
    }

    override suspend fun deleteFarmer(id: String) {
        // api.delete("farmers/$id")
    }
}
```

**Example — DI module (Koin 4.x):**

```kotlin
package tech.sumato.kmptemplate.data.farmer.di

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import tech.sumato.kmptemplate.data.farmer.mapper.FarmerMapper
import tech.sumato.kmptemplate.data.farmer.remote.FarmerApi
import tech.sumato.kmptemplate.data.farmer.repository.FarmerRepositoryImpl
import tech.sumato.kmptemplate.domain.farmer.repository.FarmerRepository

val FarmerDataModule = module {
    singleOf(::FarmerApi)
    singleOf(::FarmerMapper)
    singleOf(::FarmerRepositoryImpl) { bind<FarmerRepository>() }
}
```

### Feature — `feature/<name>/`

```
src/commonMain/kotlin/tech/sumato/kmptemplate/feature/<name>/
  presentation/
    <Name>State.kt
    <Name>Event.kt
    <Name>Effect.kt
    <Name>ViewModel.kt
    <Name>Screen.kt
  di/
    <Name>FeatureModule.kt
```

**Rules** (per `screen_architecture.md`, `state_management.md`, `viewmodel_rules.md`, `navigation_rules.md`):
- **State** — sealed interface: `Idle`, `Loading`, `Success(data)`, `Error(message)`. Exactly one immutable `UiState` per screen.
- **Event** — sealed interface: user actions (`SubmitClicked`, `FieldChanged(...)`, `Retry`)
- **Effect** — sealed interface: one-time actions (`NavigateToScreen`, `ShowSnackbar(...)`). Never store effects inside UiState.
- **ViewModel** — receives `UseCases` or `Repository interfaces` (never DTOs, DAOs, HttpClient, Entity, SQL)
- **Screen** — collects `StateFlow`, calls `viewModel.onEvent()`, collects `Channel<Effect>` in `LaunchedEffect`
- Forms use immutable state (data class inside UiState), not scattered `mutableStateOf`
- Features **never navigate directly** — they emit Effects, the app layer performs navigation

**Example — state:**

```kotlin
package tech.sumato.kmptemplate.feature.farmer.registration.presentation

import tech.sumato.kmptemplate.domain.farmer.model.Farmer

sealed interface FarmerRegistrationState {
    data object Idle : FarmerRegistrationState
    data object Loading : FarmerRegistrationState
    data class Success(val farmer: Farmer) : FarmerRegistrationState
    data class Error(val message: String) : FarmerRegistrationState
}
```

**Example — event:**

```kotlin
package tech.sumato.kmptemplate.feature.farmer.registration.presentation

sealed interface FarmerRegistrationEvent {
    data class NameChanged(val name: String) : FarmerRegistrationEvent
    data class PhoneChanged(val phone: String) : FarmerRegistrationEvent
    data class VillageChanged(val village: String) : FarmerRegistrationEvent
    data object Submit : FarmerRegistrationEvent
    data object Retry : FarmerRegistrationEvent
}
```

**Example — effect:**

```kotlin
package tech.sumato.kmptemplate.feature.farmer.registration.presentation

sealed interface FarmerRegistrationEffect {
    data object NavigateToFarmerList : FarmerRegistrationEffect
    data class ShowSnackbar(val message: String) : FarmerRegistrationEffect
}
```

**Example — ViewModel:**

```kotlin
package tech.sumato.kmptemplate.feature.farmer.registration.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import tech.sumato.kmptemplate.domain.farmer.model.Farmer
import tech.sumato.kmptemplate.domain.farmer.usecase.SaveFarmerUseCase

class FarmerRegistrationViewModel(
    private val saveFarmerUseCase: SaveFarmerUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<FarmerRegistrationState>(FarmerRegistrationState.Idle)
    val state: StateFlow<FarmerRegistrationState> = _state.asStateFlow()

    private val _effects = Channel<FarmerRegistrationEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onEvent(event: FarmerRegistrationEvent) {
        when (event) {
            is FarmerRegistrationEvent.Submit -> register()
            is FarmerRegistrationEvent.Retry -> register()
            else -> { /* update form fields */ }
        }
    }

    private fun register() {
        viewModelScope.launch {
            _state.value = FarmerRegistrationState.Loading
            try {
                val farmer = saveFarmerUseCase(
                    Farmer(id = "", name = "", phone = "", village = "")
                )
                _state.value = FarmerRegistrationState.Success(farmer)
                _effects.send(FarmerRegistrationEffect.NavigateToFarmerList)
            } catch (e: Exception) {
                _state.value = FarmerRegistrationState.Error(e.message ?: "Failed")
                _effects.send(FarmerRegistrationEffect.ShowSnackbar("Registration failed"))
            }
        }
    }
}
```

**Example — DI module:**

```kotlin
package tech.sumato.kmptemplate.feature.farmer.registration.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import tech.sumato.kmptemplate.domain.farmer.usecase.SaveFarmerUseCase
import tech.sumato.kmptemplate.feature.farmer.registration.presentation.FarmerRegistrationViewModel

val FarmerRegistrationFeatureModule = module {
    factoryOf(::SaveFarmerUseCase)
    factoryOf(::FarmerRegistrationViewModel)
}
```

---

## Step 5: Wire DI in the Shared Aggregator

**`shared/build.gradle.kts`** — add dependencies:

```kotlin
commonMain.dependencies {
    implementation(projects.feature.farmerRegistration)
    implementation(projects.data.farmer)
    implementation(projects.domain.farmer)
    implementation(projects.core.network)
    // ... existing deps
}
```

**`shared/src/commonMain/.../App.kt`** — register DI modules:

```kotlin
import tech.sumato.kmptemplate.core.network.di.NetworkModule
import tech.sumato.kmptemplate.data.farmer.di.FarmerDataModule
import tech.sumato.kmptemplate.feature.farmer.registration.di.FarmerRegistrationFeatureModule

@Composable
fun App() {
    KoinApplication(application = {
        modules(
            NetworkModule,
            FarmerDataModule,
            FarmerRegistrationFeatureModule,
            // ... all other modules
        )
    }) {
        KMPTemplateTheme {
            // Root navigation or default screen
        }
    }
}
```

---

## Reference: Existing Modules to Copy

| Pattern | Reference Module |
|---|---|
| Domain (models + repo interface + use case) | `domain/user/`, `domain/dashboard/` |
| Data (DTOs + API + mapper + repo impl + DI) | `data/user/` |
| Feature (State/Event/Effect/VM/Screen + DI) | `feature/login/` |
| No-network feature (repo returns dummy data) | `data/dashboard/` |
| Design system component | `designsystem/components/AppCard.kt` |
| Core infrastructure module | `core/network/` |

---

## Verification Checklist

Before submitting, verify every rule:

```
☐ Dependency rules: Feature→Domain, Data→Domain, Domain→Core
☐ No violations: Feature→Data, Feature→Feature, Domain→Data, Core→Domain
☐ DTOs only in data/, never in ViewModel/UI/Domain
☐ Repository interface in Domain, implementation in Data
☐ Repository returns domain models only (never DTO/Entity)
☐ Domain never has DTO, Entity, SQL, API, Compose UI, ViewModel
☐ Feature never has business models, repository impls, DTOs, entities
☐ Feature consumes Domain via UseCases or Repository interfaces
☐ ViewModel receives UseCases or Repository interfaces (not DTO/DAO/HttpClient/Entity/SQL)
☐ State is immutable sealed interface (Idle/Loading/Success/Error)
☐ Events are sealed interface
☐ Effects are sealed interface (one-time actions, not stored in state)
☐ Forms use immutable state (single UiState data class), not mutableStateOf in composables
☐ Navigation is via Effects (features never navigate directly)
☐ DI per module, combined in App.kt
☐ Module registered in settings.gradle.kts
☐ Module dependencies added in shared/build.gradle.kts
☐ All targets compile (JVM, Android, JS, iOS)
```
