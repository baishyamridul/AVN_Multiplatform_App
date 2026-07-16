package tech.sumato.kmptemplate.feature.map_analytics.presentation

sealed interface MapAnalyticsEffect {
    data class ShowSnackbar(val message: String) : MapAnalyticsEffect
}
