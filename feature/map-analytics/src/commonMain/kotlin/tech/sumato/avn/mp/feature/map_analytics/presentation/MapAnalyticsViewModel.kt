package tech.sumato.avn.mp.feature.map_analytics.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import tech.sumato.avn.mp.core.navigation.MviViewModel

class MapAnalyticsViewModel : ViewModel(), MviViewModel<MapAnalyticsState, MapAnalyticsEffect> {

    private val _state = MutableStateFlow<MapAnalyticsState>(MapAnalyticsState.Loading)
    override val state: StateFlow<MapAnalyticsState> = _state.asStateFlow()

    private val _effects = Channel<MapAnalyticsEffect>(Channel.BUFFERED)
    override val effects: Flow<MapAnalyticsEffect> = _effects.receiveAsFlow()

    init {
        loadMap()
    }

    fun onEvent(event: MapAnalyticsEvent) {
        when (event) {
            MapAnalyticsEvent.Refresh -> loadMap()
        }
    }

    private fun loadMap() {
        _state.value = MapAnalyticsState.Ready(
            styleUrl = """
                {
                  "version": 8,
                  "name": "MapLibre",
                  "center": [93.7520957, 25.55711865],
                  "zoom": 8.0,
                  "sources": {
                    "osm": {
                      "type": "raster",
                      "tiles": ["https://a.tile.openstreetmap.org/{z}/{x}/{y}.png"],
                      "tileSize": 256
                    }
                  },
                  "layers": [
                    {
                      "id": "osmLayer",
                      "type": "raster",
                      "source": "osm"
                    }
                  ]
                }
            """.trimIndent(),
        )
    }
}
