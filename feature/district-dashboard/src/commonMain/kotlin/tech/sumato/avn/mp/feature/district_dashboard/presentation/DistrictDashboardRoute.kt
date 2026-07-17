package tech.sumato.avn.mp.feature.district_dashboard.presentation

import androidx.compose.runtime.Composable
import tech.sumato.avn.mp.core.navigation.BaseRoute

@Composable
fun DistrictDashboardRoute(
    onShowSnackbar: (String) -> Unit = {},
) {
    BaseRoute<DistrictDashboardViewModel, DistrictDashboardState, DistrictDashboardEffect>(
        onEffect = { effect ->
            when (effect) {
                is DistrictDashboardEffect.ShowSnackbar -> onShowSnackbar(effect.message)
            }
        },
        content = { state ->
            DistrictDashboardScreen(
                state = state,
                onEvent = ::onEvent,
            )
        },
    )
}
