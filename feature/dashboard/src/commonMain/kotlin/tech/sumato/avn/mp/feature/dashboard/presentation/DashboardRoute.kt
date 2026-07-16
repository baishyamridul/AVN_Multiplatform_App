package tech.sumato.avn.mp.feature.dashboard.presentation

import androidx.compose.runtime.Composable
import tech.sumato.avn.mp.core.navigation.BaseRoute

@Composable
fun DashboardRoute(
    onShowSnackbar: (String) -> Unit = {},
) {
    BaseRoute<DashboardViewModel, DashboardState, DashboardEffect>(
        onEffect = { effect ->
            when (effect) {
                is DashboardEffect.ShowSnackbar -> onShowSnackbar(effect.message)
            }
        },
        content = { state ->
            DashboardScreen(
                state = state,
                onEvent = ::onEvent,
            )
        },
    )
}
