package tech.sumato.avn.mp.feature.school_dashboard.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import org.maplibre.compose.expressions.ast.Expression
import org.maplibre.compose.expressions.dsl.Feature.get
import org.maplibre.compose.expressions.dsl.asBoolean
import org.maplibre.compose.expressions.dsl.asString
import org.maplibre.compose.expressions.dsl.case
import org.maplibre.compose.expressions.dsl.condition
import org.maplibre.compose.expressions.dsl.const
import org.maplibre.compose.expressions.dsl.contains
import org.maplibre.compose.expressions.dsl.feature
import org.maplibre.compose.expressions.dsl.format
import org.maplibre.compose.expressions.dsl.offset
import org.maplibre.compose.expressions.dsl.sp
import org.maplibre.compose.expressions.dsl.span
import org.maplibre.compose.expressions.dsl.switch
import org.maplibre.compose.expressions.value.ExpressionType
import org.maplibre.compose.expressions.value.ExpressionValue
import org.maplibre.compose.expressions.value.SymbolAnchor
import org.maplibre.compose.layers.CircleLayer
import org.maplibre.compose.layers.SymbolLayer
import org.maplibre.compose.sources.GeoJsonData
import org.maplibre.compose.sources.rememberGeoJsonSource
import org.maplibre.compose.util.ClickResult
import org.maplibre.spatialk.geojson.Feature
import org.maplibre.spatialk.geojson.FeatureCollection
import org.maplibre.spatialk.geojson.Point
import tech.sumato.avn.mp.feature.school_dashboard.presentation.event.SchoolDashboardEvent
import tech.sumato.avn.mp.feature.school_dashboard.presentation.model.SchoolCategoryUiModel
import tech.sumato.avn.mp.feature.school_dashboard.presentation.model.SchoolUiModel
import kotlin.time.Duration.Companion.milliseconds

private const val POINTS_CHUNK_SIZE = 50
private const val POINTS_CHUNK_DELAY_MS = 16L

private const val PROPERTY_SELECTED = "selected"

private const val CATEGORY = "category"

private const val SCHOOL_ID = "school_id"

private const val SCHOOL_NAME = "school_name"


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
    onEvent: (event: SchoolDashboardEvent) -> Unit = {},
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
                    put(SCHOOL_ID, school.id)
                    put(SCHOOL_NAME, school.name)
                },
                id = JsonPrimitive(school.id),
            )
        }
    }

    val source = rememberGeoJsonSource(
        data = GeoJsonData.Features(FeatureCollection(features)),
    )

    val categoryColorByKey = remember {
        categoryColors
    }

    val categoryCases = categoryColorByKey.map { (key, color) ->
        case(label = key, output = const(color))
    }

    CircleLayer(
        id = "schoolsLayer",
        source = source,
//        color = switch(
//            condition(
//                test = feature[PROPERTY_SELECTED].asBoolean(),
//                output = const(Color.Blue),
//            ),
//            fallback = switch(
//                input = feature[CATEGORY].asString(),
//                *categoryCases.toTypedArray(),
//                fallback = const(Color.Red),
//            ),
//        ),
        color = switch(
            input = feature[CATEGORY].asString(),
            *categoryCases.toTypedArray(),
            fallback = const(Color.Red)
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
        onClick = { features ->
            val schoolId =
                features.firstOrNull()?.properties?.get(SCHOOL_ID)?.jsonPrimitive?.content ?: ""
            onEvent(SchoolDashboardEvent.SelectSchool(schoolId = schoolId))
            onEvent(SchoolDashboardEvent.LoadSchoolDetails(schoolId))
            return@CircleLayer ClickResult.Consume
        }
    )

    SymbolLayer(
        id = "schoolLabelLayer",
        source = source,
        filter = feature[PROPERTY_SELECTED].asBoolean(),
        textField = format(span(feature[SCHOOL_NAME].asString())),
        textFont = const(listOf("Noto Sans Regular")),
        textSize = const(12f).sp,
        textColor = const(Color.White),
        textHaloColor = const(Color.Black),
        textHaloWidth = const(1.dp),
        textAnchor = const(SymbolAnchor.Top),
        textOffset = offset(0f.sp, 10f.sp),
        textAllowOverlap = const(true),

        )

}