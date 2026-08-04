package tech.sumato.avn.mp.feature.map_analytics.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import tech.sumato.avn.mp.designsystem.FormFactor
import tech.sumato.avn.mp.designsystem.LocalFormFactor
import tech.sumato.avn.mp.designsystem.components.AppCard
import tech.sumato.avn.mp.component.map.MapView
import tech.sumato.avn.mp.designsystem.theme.MainColor
import tech.sumato.avn.mp.feature.map_analytics.presentation.event.MapAnalyticsEvent
import tech.sumato.avn.mp.feature.map_analytics.presentation.state.MapAnalyticsState

@Composable
fun MapAnalyticsScreen(
    state: MapAnalyticsState,
    onEvent: (MapAnalyticsEvent) -> Unit,
) {
    when (val s = state) {
        is MapAnalyticsState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }
        is MapAnalyticsState.Ready -> {
            when (LocalFormFactor.current) {
                FormFactor.Compact -> MapCompact(s.styleUrl)
                FormFactor.Medium -> MapWide(s.styleUrl)
                FormFactor.Expanded -> MapWide(s.styleUrl)
            }
        }
        is MapAnalyticsState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(s.message)
            }
        }
    }
}

@Composable
private fun MapCompact(styleUrl: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        MapView(
            modifier = Modifier.fillMaxSize(),
            styleUrl = styleUrl,
        ) {

        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
        ) {
            MapFloatingButton(Icons.Default.MyLocation, "My Location")
            Spacer(Modifier.height(8.dp))
            MapFloatingButton(Icons.Default.Layers, "Layers")
        }
    }
}

@Composable
private fun MapWide(styleUrl: String) {
    Row(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            MapView(
                modifier = Modifier.fillMaxSize(),
                styleUrl = styleUrl,
            ) {

            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
            ) {
                MapFloatingButton(Icons.Default.MyLocation, "My Location")
                Spacer(Modifier.height(8.dp))
                MapFloatingButton(Icons.Default.Layers, "Layers")
            }
        }

        Column(
            modifier = Modifier
                .width(280.dp)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        ) {
            MapSidePanel()
        }
    }
}

@Composable
private fun MapFloatingButton(icon: androidx.compose.ui.graphics.vector.ImageVector, contentDescription: String) {
    FloatingActionButton(
        onClick = { },
        modifier = Modifier.size(44.dp),
        shape = RoundedCornerShape(12.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MainColor,
    ) {
        Icon(icon, contentDescription)
    }
}

@Composable
private fun MapSidePanel() {
    AppCard {
        Text(
            "Map Legend",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 12.dp),
        )

        LegendItem("District Boundary", MainColor)
        LegendItem("Water Body", MainColor.copy(alpha = 0.6f))
        LegendItem("Green Zone", MainColor.copy(alpha = 0.4f))
        LegendItem("Development Zone", MainColor.copy(alpha = 0.8f))
    }

    Spacer(Modifier.height(12.dp))

    AppCard {
        Text(
            "Filters",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 12.dp),
        )
        Text(
            "District Type",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            "Show all",
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
private fun LegendItem(label: String, color: androidx.compose.ui.graphics.Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(RoundedCornerShape(2.dp)),
        ) {
            androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                drawRect(color = color)
            }
        }
        Spacer(Modifier.width(8.dp))
        Text(
            label,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}
