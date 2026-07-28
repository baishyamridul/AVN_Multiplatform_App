package tech.sumato.avn.mp.component.panorama

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun PanoramaViewer(
    configUrl: String,
    modifier: Modifier = Modifier,
    state: PanoramaState = rememberPanoramaState(),
    onLoadingChanged: (Boolean) -> Unit = {},
    onError: (Throwable) -> Unit = {},
)
