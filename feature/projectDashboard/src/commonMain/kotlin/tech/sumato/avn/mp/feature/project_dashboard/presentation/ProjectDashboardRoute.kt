package tech.sumato.avn.mp.feature.project_dashboard.presentation

import androidx.compose.runtime.Composable
import tech.sumato.avn.mp.core.navigation.BaseRoute
import tech.sumato.avn.mp.feature.project_dashboard.presentation.effect.ProjectDashboardEffect
import tech.sumato.avn.mp.feature.project_dashboard.presentation.state.ProjectDashboardState

@Composable
fun ProjectDashboardRoute(
    onShowSnackbar: (String) -> Unit = {},
) {
    BaseRoute<ProjectDashboardViewModel, ProjectDashboardState, ProjectDashboardEffect>(
        onEffect = { effect ->
            when (effect) {
                is ProjectDashboardEffect.ShowSnackbar -> onShowSnackbar(effect.message)
            }
        },
        content = { state ->
            ProjectDashboardScreen(
                state = state,
                onEvent = ::onEvent,
            )
        },
    )
}
