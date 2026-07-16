package tech.sumato.kmptemplate.feature.dashboard.presentation

import androidx.compose.runtime.Composable
import tech.sumato.kmptemplate.core.navigation.BaseRoute

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
