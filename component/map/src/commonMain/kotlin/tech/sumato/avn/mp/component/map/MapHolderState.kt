package tech.sumato.avn.mp.component.map

import org.maplibre.spatialk.geojson.BoundingBox
import org.maplibre.spatialk.geojson.Position


data class MapHolderCameraState(
    val latitude: Double? = 25.55711865,
    val longitude: Double? = 93.7520957,
    val boundingBox: MapHolderBoundingBox? = null,
    val zoom: Double = 8.0,
    val bearing: Double = 0.0,
    val tilt: Double = 0.0,
)

data class MapHolderBoundingBox(
    val west: Double = 91.20,
    val south: Double = 26.28,
    val east: Double = 97.30,
    val north: Double = 29.30,
)

fun MapHolderBoundingBox.toBoundingBox(): BoundingBox {
    return BoundingBox(west, south, east, north)
}


data class MapHolderState(
    val cameraState: MapHolderCameraState = MapHolderCameraState()
)
