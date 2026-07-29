package tech.sumato.avn.mp.component.map

import android.graphics.Insets.add
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import kotlinx.serialization.json.add
import kotlinx.serialization.json.addJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import kotlinx.serialization.json.putJsonObject
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.camera.CameraState
import org.maplibre.compose.map.MapOptions
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.map.OrnamentOptions
import org.maplibre.compose.map.RenderOptions
import org.maplibre.compose.style.BaseStyle
import org.maplibre.spatialk.geojson.Position

@Composable
actual fun MapView(
    modifier: Modifier,
    styleUrl: String,
) {


    val mapBaseStyle = remember() {
        BaseStyle.Json {
            put("glyphs", "https://demotiles.maplibre.org/font/{fontstack}/{range}.pbf")
            putJsonObject("sources") {
                putJsonObject("osm") {
                    put("type", "raster")
                    putJsonArray("tiles") {
                        add("https://a.tile.openstreetmap.org/{z}/{x}/{y}.png")
                    }
                    put("tileSize", 256)
                    put("attribution", "&copy; OpenStreetMap Contributors")
                    put("maxzoom", 19)
                }
                putJsonObject("satellite") {
                    put("type", "raster")
                    putJsonArray("tiles") {
                        add("https://mt1.google.com/vt/lyrs=s&x={x}&y={y}&z={z}")
                    }
                    put("tileSize", 256)
                }
            }
            putJsonArray("layers") {
                addJsonObject {
                    put("id", "background")
                    put("type", "background")
                    putJsonObject("paint") {
                        put("background-color", "#1e2939")
                    }
                }
                addJsonObject {
                    put("id", "osmLayer")
                    put("type", "raster")
                    put("source", "osm")
                    putJsonObject("layout") {
                        put("visibility", "visible")
                    }
                }
                addJsonObject {
                    put("id", "satelliteLayer")
                    put("type", "raster")
                    put("source", "satellite")
                    putJsonObject("layout") {
                        put("visibility", "none")
                    }
                }
            }
        }
    }

//    MaplibreMap(
//        modifier = modifier,
//        baseStyle = mapBaseStyle,
//        options = MapOptions(
//            renderOptions = RenderOptions(),
//            ornamentOptions = OrnamentOptions(
//                isLogoEnabled = false,
//                isAttributionEnabled = false
//            )
//        ),
//        cameraState = CameraState(
//            firstPosition = CameraPosition(
//                target = Position(longitude = 93.609159, latitude = 27.085558),
//                zoom = 8.0,
//                bearing = 0.0,
//                tilt = 0.0,
//            ),
//        ),
//
//    )

    Box(modifier = modifier) {
        Text("Android emulator does not support")
    }

}
