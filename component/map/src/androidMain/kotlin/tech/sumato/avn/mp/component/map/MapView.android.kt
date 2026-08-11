package tech.sumato.avn.mp.component.map

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.camera.CameraState
import org.maplibre.compose.map.MapOptions
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.map.OrnamentOptions
import org.maplibre.compose.map.RenderOptions
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

    MaplibreMap(
        modifier = modifier,
        baseStyle = mapBaseStyle,
        options = MapOptions(
            renderOptions = RenderOptions(),
            ornamentOptions = OrnamentOptions(
                isLogoEnabled = false,
                isAttributionEnabled = false
            )
        ),
        cameraState = CameraState(
            firstPosition = CameraPosition(
                target = Position(longitude = 93.609159, latitude = 27.085558),
                zoom = 8.0,
                bearing = 0.0,
                tilt = 0.0,
            ),
        ),

    ) {
        layers()
    }

//    Box(modifier = modifier) {
//        Text("Android emulator does not support")
//    }

}
