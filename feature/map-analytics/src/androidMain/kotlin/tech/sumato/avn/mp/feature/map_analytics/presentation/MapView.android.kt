package tech.sumato.avn.mp.feature.map_analytics.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.camera.CameraState
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.style.BaseStyle
import org.maplibre.spatialk.geojson.Position

@Composable
actual fun MapView(
    modifier: Modifier,
    styleUrl: String,
) {
    MaplibreMap(
        modifier = modifier,
        baseStyle = BaseStyle.Json(styleUrl),
        cameraState = CameraState(
            firstPosition = CameraPosition(
                target = Position(longitude = 93.7520957, latitude = 25.55711865),
                zoom = 8.0,
                bearing = 0.0,
                tilt = 0.0,
            ),
        ),
    )
}
