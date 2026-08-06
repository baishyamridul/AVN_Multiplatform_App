package tech.sumato.avn.mp.feature.school_dashboard.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.maplibre.compose.expressions.dsl.const
import org.maplibre.compose.layers.CircleLayer
import org.maplibre.compose.sources.GeoJsonData
import org.maplibre.compose.sources.rememberGeoJsonSource
import org.maplibre.spatialk.geojson.Point
import tech.sumato.avn.mp.feature.school_dashboard.presentation.model.SchoolUiModel


@Composable
fun SchoolsMapLayers(
    schools: List<SchoolUiModel>,
    selectedSchoolId: String? = null,
) {


    schools.filter { it.hasLocation() }.forEach { school ->

        val isSelected = school.id == selectedSchoolId

        CircleLayer(
            id = "schoolLayer_${school.id}",
            source = rememberGeoJsonSource(
                GeoJsonData.Features(
                    Point(latitude = school.latitude!!, longitude = school.longitude!!)
                )
            ),
            color = if (isSelected) const(Color.Blue) else const(Color.Red),
            radius = if (isSelected) const(9.dp) else const(6.dp),
            strokeColor = const(Color.Black),
            strokeWidth = const(1.dp),
        )

    }


}
