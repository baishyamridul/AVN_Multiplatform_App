package tech.sumato.avn.mp.feature.school_dashboard.presentation

import androidx.compose.runtime.Composable
import tech.sumato.avn.mp.core.navigation.BaseRoute

@Composable
fun SchoolDashboardRoute(
    onShowSnackbar: (String) -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    BaseRoute<SchoolDashboardViewModel, SchoolDashboardState, SchoolDashboardEffect>(
        onEffect = { effect ->
            when (effect) {
                is SchoolDashboardEffect.ShowSnackbar -> onShowSnackbar(effect.message)
                is SchoolDashboardEffect.NavigateBack -> onNavigateBack()
            }
        },
        content = { state ->
            SchoolDashboardScreen(
                state = state,
                onEvent = ::onEvent,
            )
        },
    )
}
