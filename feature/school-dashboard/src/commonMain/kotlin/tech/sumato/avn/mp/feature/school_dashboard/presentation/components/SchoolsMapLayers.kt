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
import org.maplibre.compose.expressions.dsl.asString
import org.maplibre.compose.expressions.dsl.case
import org.maplibre.compose.expressions.dsl.condition
import org.maplibre.compose.expressions.dsl.const
import org.maplibre.compose.expressions.dsl.contains
import org.maplibre.compose.expressions.dsl.feature
import org.maplibre.compose.expressions.dsl.switch
import org.maplibre.compose.layers.CircleLayer
import org.maplibre.compose.sources.GeoJsonData
import org.maplibre.compose.sources.rememberGeoJsonSource
import org.maplibre.spatialk.geojson.Feature
import org.maplibre.spatialk.geojson.FeatureCollection
import org.maplibre.spatialk.geojson.Point
import tech.sumato.avn.mp.feature.school_dashboard.presentation.model.SchoolCategoryUiModel
import tech.sumato.avn.mp.feature.school_dashboard.presentation.model.SchoolUiModel
import kotlin.time.Duration.Companion.milliseconds

private const val POINTS_CHUNK_SIZE = 50
private const val POINTS_CHUNK_DELAY_MS = 16L

private const val PROPERTY_SELECTED = "selected"

private const val CATEGORY = "category"

private val CATEGORY_PALETTE = listOf(
    Color(0xffe53935),
    Color(0xff1e88e5),
    Color(0xff43a047),
    Color(0xfffdd835),
    Color(0xff8e24aa),
    Color(0xff00acc1),
    Color(0xfffb8c00),
    Color(0xffd81b60),
    Color(0xff3949ab),
    Color(0xff6d4c41),
)

fun String.isPrimarySchool(): Boolean {
    return this.lowercase() == "primary school"
}

@Composable
fun SchoolsMapLayers(
    schools: List<SchoolUiModel>,
    categories: List<SchoolCategoryUiModel> = emptyList(),
    selectedSchoolId: String? = null,
) {

    val located = remember(schools) { schools.filter { it.hasLocation() } }

    var loadedCount by remember(located) { mutableStateOf(0) }

    LaunchedEffect(located) {
        loadedCount = 0
        while (loadedCount < located.size) {
            delay(POINTS_CHUNK_DELAY_MS.milliseconds)
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
                    put(CATEGORY, school.category?.key)
                },
                id = JsonPrimitive(school.id),
            )
        }
    }

    val source = rememberGeoJsonSource(
        data = GeoJsonData.Features(FeatureCollection(features)),
    )

    val categoryColorByKey = remember(categories) {
        mapOf(
            "primarySchool_1_to_5" to Color(0xff00E5FF),
            "upperPrimarySchool_1_to_8" to Color(0xffFFD600),
            "upperPrimarySchool_6_to_8" to Color(0xffFFD600),
            "secondarySchool_1_to_10" to Color(0xff00E676),
            "secondarySchool_6_to_10" to Color(0xff00E676),
            "secondarySchool_9_to_10" to Color(0xff00E676),
            "higherSecondarySchool_6_to_12" to Color(0xffD500F9),
            "higherSecondarySchool_9_to_12" to Color(0xffD500F9),
            "higherSecondarySchool_1_to_12" to Color(0xffD500F9),
        )
//        categories
//            .sortedBy { it.key }
//            .mapIndexed { index, category ->
//                category.key to CATEGORY_PALETTE[index % CATEGORY_PALETTE.size]
//            }
//            .toMap()
    }

    val categoryCases = categoryColorByKey.map { (key, color) ->
        case(label = key, output = const(color))
    }

    CircleLayer(
        id = "schoolsLayer",
        source = source,
        color = switch(
            condition(
                test = feature[PROPERTY_SELECTED].asBoolean(),
                output = const(Color.Blue),
            ),
            fallback = switch(
                input = feature[CATEGORY].asString(),
                *categoryCases.toTypedArray(),
                fallback = const(Color.Red),
            ),
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