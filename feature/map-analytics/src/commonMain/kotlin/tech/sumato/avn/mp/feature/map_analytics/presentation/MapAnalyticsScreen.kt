package tech.sumato.avn.mp.feature.map_analytics.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun MapAnalyticsScreen(
    state: MapAnalyticsState,
    onEvent: (MapAnalyticsEvent) -> Unit,
) {
    when (val s = state) {
        is MapAnalyticsState.Loading -> {
            androidx.compose.foundation.layout.Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }
        is MapAnalyticsState.Ready -> {
            MapView(
                modifier = Modifier.fillMaxSize(),
                styleUrl = s.styleUrl,
            )
        }
        is MapAnalyticsState.Error -> {
            androidx.compose.foundation.layout.Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                androidx.compose.material3.Text(s.message)
            }
        }
    }
}
