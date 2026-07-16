package tech.sumato.avn.mp.feature.map_analytics.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun MapView(
    modifier: Modifier,
    styleUrl: String,
)
