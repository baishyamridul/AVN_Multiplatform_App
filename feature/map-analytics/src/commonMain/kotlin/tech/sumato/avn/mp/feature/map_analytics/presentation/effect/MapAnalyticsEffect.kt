package tech.sumato.avn.mp.feature.map_analytics.presentation.effect

sealed interface MapAnalyticsEffect {
    data class ShowSnackbar(val message: String) : MapAnalyticsEffect
}
