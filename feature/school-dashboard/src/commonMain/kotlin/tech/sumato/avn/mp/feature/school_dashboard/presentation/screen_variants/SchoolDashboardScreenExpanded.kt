package tech.sumato.avn.mp.feature.school_dashboard.presentation.screen_variants

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel
import tech.sumato.avn.mp.component.image360.PanoImageViewer
import tech.sumato.avn.mp.component.map.DistrictSvgMap
import tech.sumato.avn.mp.component.map.MapView
import tech.sumato.avn.mp.designsystem.components.AppCard
import tech.sumato.avn.mp.designsystem.components.AppCardBordered
import tech.sumato.avn.mp.feature.school_dashboard.presentation.SchoolDashboardViewModel
import tech.sumato.avn.mp.feature.school_dashboard.presentation.components.SchoolDashboardHeader
import tech.sumato.avn.mp.feature.school_dashboard.presentation.components.SchoolsMapLayers
import tech.sumato.avn.mp.feature.school_dashboard.presentation.event.SchoolDashboardEvent
import tech.sumato.avn.mp.feature.school_dashboard.presentation.model.toUiModel
import tech.sumato.avn.mp.feature.school_dashboard.presentation.state.SchoolDashboardState


@Composable
fun SchoolDashboardScreenExpanded(
    state: SchoolDashboardState,
    onEvent: (SchoolDashboardEvent) -> Unit
) {


    var loadMap by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(2000)

        loadMap = true

    }


    Column(
        modifier = Modifier.fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        SchoolDashboardHeader(
            modifier = Modifier.fillMaxWidth(),
            onBack = {
                onEvent(SchoolDashboardEvent.Back)
            }
        )


        HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = Dp.Hairline)

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {

            AppCardBordered(
                modifier = Modifier.weight(3.5f).fillMaxSize(),
                paddingLess = true,
            ) {

//                PanoImageViewer("https://mridx.github.io/360img/config.json")


                if (loadMap)
                    MapView(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp)),
                        styleUrl = "",
                    ) {
                        SchoolsMapLayers(schools = state.schoolsState.schools.map { it.toUiModel() })
                    }
                else
                    Box(
                        modifier = Modifier.fillMaxSize()
                            .background(MaterialTheme.colorScheme.surface)
                    )
            }

            AppCardBordered(
                modifier = Modifier.weight(1.5f).fillMaxSize(),
                paddingLess = false,
            ) {
                Text(
                    "\uD83C\uDFEB Schools",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(state.schoolsState.schools) { school ->
                        AppCardBordered(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
                            Text(
                                school.name,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "${school.category?.name ?: ""} \u2022 ${school.district.name}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }


            }

        }


    }

}