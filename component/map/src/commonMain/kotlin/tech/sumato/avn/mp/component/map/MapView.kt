package tech.sumato.avn.mp.component.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.serialization.json.add
import kotlinx.serialization.json.addJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import kotlinx.serialization.json.putJsonObject
import org.maplibre.compose.style.BaseStyle
import org.maplibre.compose.util.MaplibreComposable


val mapBaseStyle =
    BaseStyle.Json {
        put("version", 8)
        put("name", "AVN Base Style")
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


@Composable
expect fun MapView(
    modifier: Modifier,
    styleUrl: String,
    layers: @Composable @MaplibreComposable () -> Unit
)
