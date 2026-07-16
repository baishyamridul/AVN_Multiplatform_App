package tech.sumato.avn.mp.feature.map_analytics.presentation

sealed interface MapAnalyticsState {
    data object Loading : MapAnalyticsState
    data object Idle : MapAnalyticsState
    data class Error(val message: String) : MapAnalyticsState
}
