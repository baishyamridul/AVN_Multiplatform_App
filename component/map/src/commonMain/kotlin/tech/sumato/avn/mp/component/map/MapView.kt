package tech.sumato.avn.mp.component.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun MapView(
    modifier: Modifier,
    styleUrl: String,
)
