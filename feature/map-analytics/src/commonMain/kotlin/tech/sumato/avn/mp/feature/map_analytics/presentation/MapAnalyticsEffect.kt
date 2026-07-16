package tech.sumato.avn.mp.feature.map_analytics.presentation

sealed interface MapAnalyticsEffect {
    data class ShowSnackbar(val message: String) : MapAnalyticsEffect
}
