package tech.sumato.avn.mp.feature.map_analytics.presentation.state

sealed interface MapAnalyticsState {
    data object Loading : MapAnalyticsState
    data class Ready(val styleUrl: String) : MapAnalyticsState
    data class Error(val message: String) : MapAnalyticsState
}
