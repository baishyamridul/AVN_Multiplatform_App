[//]: # (Navigation)

[//]: # ()
[//]: # (Features never navigate directly.)

[//]: # ()
[//]: # (Features emit Effects.)

[//]: # ()
[//]: # (Application layer performs navigation.)

[//]: # ()
[//]: # (⸻)


Navigation

Navigation follows a Route-based architecture.

• Features never perform navigation directly.
• ViewModels never depend on NavController or navigation libraries.
• ViewModels emit one-time Effects (NavigateTo..., Open..., Back, etc.).
• Every destination must have a dedicated *Route.kt composable.
• The Route owns the ViewModel and uses the shared BaseRoute helper.
• BaseRoute is responsible for:
- obtaining the ViewModel from Koin
- collecting State
- collecting Effects
- invoking navigation callbacks
- handling lifecycle-aware collection
• *Screen.kt must be a pure UI composable. It must not know about Koin, ViewModels, Navigation, or StateFlow.
• NavGraph only declares destinations and provides navigation callbacks to each Route.
• Navigation is executed by the Route through callbacks supplied by the NavGraph, never by the ViewModel.

Flow:

NavGraph
↓
LoginRoute
↓
BaseRoute
↓
LoginViewModel
↓
LoginScreen

Navigation flow:

User Action
↓
LoginScreen
↓
LoginEvent
↓
LoginViewModel
↓
LoginEffect.NavigateToDashboard
↓
BaseRoute
↓
onNavigateDashboard()
↓
NavGraph / NavController