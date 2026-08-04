package tech.sumato.avn.mp.feature.school_dashboard.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import org.maplibre.compose.expressions.dsl.const
import org.maplibre.compose.expressions.dsl.image
import org.maplibre.compose.expressions.dsl.offset
import org.maplibre.compose.expressions.value.SymbolOverlap
import org.maplibre.compose.expressions.value.SymbolPlacement
import org.maplibre.compose.layers.CircleLayer
import org.maplibre.compose.layers.SymbolLayer
import org.maplibre.compose.sources.GeoJsonData
import org.maplibre.compose.sources.rememberGeoJsonSource
import org.maplibre.compose.util.ClickResult
import org.maplibre.spatialk.geojson.Point
import org.maplibre.spatialk.geojson.Position
import tech.sumato.avn.mp.feature.school_dashboard.presentation.model.SchoolUiModel


@Composable
fun SchoolsMapLayers(schools: List<SchoolUiModel>) {


    schools.filter { it.hasLocation() }.forEach { school ->
        println(school)

        CircleLayer(
            id = "schoolLayer_${school.id}",
            source = rememberGeoJsonSource(
                GeoJsonData.Features(
                    Point(latitude = school.latitude!!, longitude = school.longitude!!)
                )
            ),
            color = const(Color.Red),
            radius = const(6.dp),
            strokeColor = const(Color.Black),
            strokeWidth = const(1.dp),

        )

    }


}