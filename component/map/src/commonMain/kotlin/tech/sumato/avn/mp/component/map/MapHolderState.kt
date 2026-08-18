package tech.sumato.avn.mp.component.map

import org.maplibre.spatialk.geojson.BoundingBox
import org.maplibre.spatialk.geojson.Position


data class MapHolderCameraState(
    val latitude: Double? = 25.55711865,
    val longitude: Double? = 93.7520957,
    val boundingBox: MapHolderBoundingBox? = null,
    val focusPosition: MapHolderPosition? = null,
    val zoom: Double = 8.0,
    val bearing: Double = 0.0,
    val tilt: Double = 0.0,
    val resetVersion: Int = 0,
)

data class MapHolderPosition(
    val latitude: Double,
    val longitude: Double,
)

data class MapHolderBoundingBox(
    val west: Double = 91.5481,
    val south: Double = 26.6071,
    val east: Double = 97.3731,
    val north: Double = 29.3490,
)

fun MapHolderBoundingBox.toBoundingBox(): BoundingBox {
    return BoundingBox(west, south, east, north)
}


data class MapHolderState(
    val cameraState: MapHolderCameraState = MapHolderCameraState()
)
