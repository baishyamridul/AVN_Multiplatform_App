package tech.sumato.avn.mp.component.map

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.expressions.dsl.const
import org.maplibre.compose.layers.LineLayer
import org.maplibre.compose.map.GestureOptions
import org.maplibre.compose.map.MapOptions
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.map.OrnamentOptions
import org.maplibre.compose.map.RenderOptions
import org.maplibre.compose.sources.GeoJsonData
import org.maplibre.compose.sources.rememberGeoJsonSource
import org.maplibre.compose.style.BaseStyle
import org.maplibre.compose.style.rememberStyleState
import org.maplibre.compose.util.MaplibreComposable
import org.maplibre.spatialk.geojson.Position
import kotlin.time.Duration.Companion.milliseconds


@Composable
fun MapHolder(
    modifier: Modifier,
    mapHolderState: MapHolderState = MapHolderState(),
    disable: Boolean = false,
    layers: @Composable @MaplibreComposable () -> Unit
) {

    val cameraState = rememberCameraState(
        firstPosition = CameraPosition(
            target = Position(
                longitude = mapHolderState.cameraState.longitude ?: 93.609159,
                latitude = mapHolderState.cameraState.latitude ?: 27.085558
            ),
            zoom = 8.0,
            bearing = 0.0,
            tilt = 0.0,
        ),
    )



    LaunchedEffect(
        mapHolderState.cameraState.boundingBox,
        mapHolderState.cameraState.focusPosition,
    ) {
        val focusPosition = mapHolderState.cameraState.focusPosition
        if (focusPosition != null) {
            cameraState.animateTo(
                finalPosition = CameraPosition(
                    target = Position(
                        longitude = focusPosition.longitude,
                        latitude = focusPosition.latitude,
                    ),
                    zoom = 14.0,
                    bearing = 0.0,
                    tilt = 0.0,
                ),
                duration = 800.milliseconds,
            )
        } else {
            val boundingBox = mapHolderState.cameraState.boundingBox
            if (boundingBox != null) {
                cameraState.animateTo(
                    boundingBox = boundingBox.toBoundingBox(),
                    padding = PaddingValues(16.dp),
                    tilt = 0.0,
                    bearing = 0.0
                )
            }
        }
    }


    val arunachalBoundary by produceState<String?>(initialValue = null) {
        value = GeoJsonLoader.loadArunachalBoundary()
    }
//
//    LaunchedEffect(Unit) {
//        val json = GeoJsonLoader.loadArunachalBoundary()
//        println(json)
//    }


    MaplibreMap(
        modifier = modifier.clip(RoundedCornerShape(8.dp)),
//        baseStyle = mapBaseStyle,
        baseStyle = BaseStyle.Uri("https://api.protomaps.com/styles/v5/dark/en.json?key=73c45a97eddd43fb"),
        cameraState = cameraState,

        styleState = rememberStyleState(),
        options = MapOptions(
            gestureOptions = if (disable) GestureOptions.AllDisabled else GestureOptions.Standard,
            ornamentOptions = OrnamentOptions(
                isLogoEnabled = false,
                isCompassEnabled = false,
                isScaleBarEnabled = false
            ),
            renderOptions = RenderOptions.Standard
        )
    ) {
        val arunachalBoundarySource = rememberGeoJsonSource(
            data = GeoJsonData.JsonString(arunachalBoundary ?: "{}")
        )

        LineLayer(
            id = "boundryLayer",
            source = arunachalBoundarySource,
            color = const(Color.White)
        )

        layers()
    }


}