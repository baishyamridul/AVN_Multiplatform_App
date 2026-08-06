package tech.sumato.avn.mp.feature.map_analytics.presentation.event

sealed interface MapAnalyticsEvent {
    data object Refresh : MapAnalyticsEvent
}
