package tech.sumato.avn.mp.feature.map_analytics.presentation

import androidx.compose.runtime.Composable
import tech.sumato.avn.mp.core.navigation.BaseRoute

@Composable
fun MapAnalyticsRoute(
    onShowSnackbar: (String) -> Unit = {},
) {
    BaseRoute<MapAnalyticsViewModel, MapAnalyticsState, MapAnalyticsEffect>(
        onEffect = { effect ->
            when (effect) {
                is MapAnalyticsEffect.ShowSnackbar -> onShowSnackbar(effect.message)
            }
        },
        content = { state ->
            MapAnalyticsScreen(
                state = state,
                onEvent = ::onEvent,
            )
        },
    )
}
