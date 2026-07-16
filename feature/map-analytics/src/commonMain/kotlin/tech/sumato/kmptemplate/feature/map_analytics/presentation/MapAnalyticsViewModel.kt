package tech.sumato.kmptemplate.feature.map_analytics.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import tech.sumato.kmptemplate.core.navigation.MviViewModel

class MapAnalyticsViewModel : ViewModel(), MviViewModel<MapAnalyticsState, MapAnalyticsEffect> {

    private val _state = MutableStateFlow<MapAnalyticsState>(MapAnalyticsState.Loading)
    override val state: StateFlow<MapAnalyticsState> = _state.asStateFlow()

    private val _effects = Channel<MapAnalyticsEffect>(Channel.BUFFERED)
    override val effects: Flow<MapAnalyticsEffect> = _effects.receiveAsFlow()

    fun onEvent(event: MapAnalyticsEvent) {
        when (event) {
            else -> {}
        }
    }
}
