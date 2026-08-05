package tech.sumato.avn.mp.feature.school_dashboard.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import tech.sumato.avn.mp.core.navigation.BaseRoute
import tech.sumato.avn.mp.feature.school_dashboard.presentation.effect.SchoolDashboardEffect
import tech.sumato.avn.mp.feature.school_dashboard.presentation.state.SchoolDashboardState

@Composable
fun SchoolDashboardRoute(
    initialDistrictId: Int? = null,
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
            LaunchedEffect(initialDistrictId) {
                initialDistrictId?.let { preselectDistrict(it) }
            }
            SchoolDashboardScreen(
                state = state,
                onEvent = ::onEvent,
            )
        },
    )
}
