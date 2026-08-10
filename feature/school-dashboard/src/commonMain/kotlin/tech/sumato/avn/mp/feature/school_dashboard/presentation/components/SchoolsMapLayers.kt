package tech.sumato.avn.mp.feature.school_dashboard.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.maplibre.compose.expressions.dsl.asBoolean
import org.maplibre.compose.expressions.dsl.condition
import org.maplibre.compose.expressions.dsl.const
import org.maplibre.compose.expressions.dsl.feature
import org.maplibre.compose.expressions.dsl.switch
import org.maplibre.compose.layers.CircleLayer
import org.maplibre.compose.sources.GeoJsonData
import org.maplibre.compose.sources.rememberGeoJsonSource
import org.maplibre.spatialk.geojson.Feature
import org.maplibre.spatialk.geojson.FeatureCollection
import org.maplibre.spatialk.geojson.Point
import tech.sumato.avn.mp.feature.school_dashboard.presentation.model.SchoolUiModel

private const val POINTS_CHUNK_SIZE = 50
private const val POINTS_CHUNK_DELAY_MS = 16L

private const val PROPERTY_SELECTED = "selected"

@Composable
fun SchoolsMapLayers(
    schools: List<SchoolUiModel>,
    selectedSchoolId: String? = null,
) {

    val located = remember(schools) { schools.filter { it.hasLocation() } }

    var loadedCount by remember(located) { mutableStateOf(0) }

    LaunchedEffect(located) {
        loadedCount = 0
        while (loadedCount < located.size) {
            delay(POINTS_CHUNK_DELAY_MS)
            loadedCount = (loadedCount + POINTS_CHUNK_SIZE).coerceAtMost(located.size)
        }
    }

    if (loadedCount == 0) return

    val features = remember(located, loadedCount, selectedSchoolId) {
        located.take(loadedCount).map { school ->
            Feature(
                geometry = Point(
                    latitude = school.latitude!!,
                    longitude = school.longitude!!,
                ),
                properties = buildJsonObject {
                    put(PROPERTY_SELECTED, school.id == selectedSchoolId)
                },
                id = JsonPrimitive(school.id),
            )
        }
    }

    val source = rememberGeoJsonSource(
        data = GeoJsonData.Features(FeatureCollection(features)),
    )

    CircleLayer(
        id = "schoolsLayer",
        source = source,
        color = switch(
            condition(
                test = feature[PROPERTY_SELECTED].asBoolean(),
                output = const(Color.Blue),
            ),
            fallback = const(Color.Red),
        ),
        radius = switch(
            condition(
                test = feature[PROPERTY_SELECTED].asBoolean(),
                output = const(9.dp),
            ),
            fallback = const(6.dp),
        ),
        strokeColor = const(Color.Black),
        strokeWidth = const(1.dp),
    )
}