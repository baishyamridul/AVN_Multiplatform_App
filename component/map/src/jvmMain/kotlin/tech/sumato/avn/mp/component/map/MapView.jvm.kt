package tech.sumato.avn.mp.component.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.camera.CameraState
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.map.MapOptions
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.map.RenderOptions
import org.maplibre.compose.style.rememberStyleState
import org.maplibre.compose.util.MaplibreComposable
import org.maplibre.spatialk.geojson.BoundingBox
import org.maplibre.spatialk.geojson.Position

@Composable
actual fun MapView(
    modifier: Modifier,
    styleUrl: String,
    boundingBox: BoundingBox?,
    layers: @Composable @MaplibreComposable () -> Unit
) {


    val cameraState = rememberCameraState(
        firstPosition = CameraPosition(
            target = Position(longitude = 93.609159, latitude = 27.085558),
            zoom = 8.0,
            bearing = 0.0,
            tilt = 0.0,
        ),
    )

    MaplibreMap(
        modifier = modifier.clip(RoundedCornerShape(8.dp)),
        baseStyle = mapBaseStyle,
        cameraState = cameraState,
        styleState = rememberStyleState(),
        options = MapOptions(
        )
    ) {
        layers()
    }

}