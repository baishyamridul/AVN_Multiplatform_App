package tech.sumato.avn.mp.feature.map_analytics.presentation

sealed interface MapAnalyticsEvent {
    data object Refresh : MapAnalyticsEvent
}
