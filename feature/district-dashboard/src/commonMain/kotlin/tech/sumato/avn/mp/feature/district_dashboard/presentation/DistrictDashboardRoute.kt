package tech.sumato.avn.mp.feature.district_dashboard.presentation

import androidx.compose.runtime.Composable
import tech.sumato.avn.mp.core.navigation.BaseRoute
import tech.sumato.avn.mp.core.navigation.Route

@Composable
fun DistrictDashboardRoute(
    onShowSnackbar: (String) -> Unit = {},
    onNavigation: (route: String) -> Unit = {}
) {
    BaseRoute<DistrictDashboardViewModel, DistrictDashboardState, DistrictDashboardEffect>(
        onEffect = { effect ->
            when (effect) {
                is DistrictDashboardEffect.ShowSnackbar -> onShowSnackbar(effect.message)
                is DistrictDashboardEffect.Navigate -> onNavigation(effect.route)
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
