package tech.sumato.avn.mp.feature.district_dashboard.presentation.screen_variants

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.koin.compose.viewmodel.koinViewModel
import tech.sumato.avn.mp.feature.district_dashboard.presentation.DistrictDashboardViewModel
import tech.sumato.avn.mp.feature.district_dashboard.presentation.components.DistrictDashboardHeader
import tech.sumato.avn.mp.feature.district_dashboard.presentation.components.DistrictDashboardMap
import tech.sumato.avn.mp.feature.district_dashboard.presentation.components.DistrictDashboardSchoolCategoriesAnalytics
import tech.sumato.avn.mp.feature.district_dashboard.presentation.components.HorizontalStatsBar
import tech.sumato.avn.mp.feature.district_dashboard.presentation.components.OngoingProjects
import tech.sumato.avn.mp.feature.district_dashboard.presentation.components.UpcomingEvents
import tech.sumato.avn.mp.feature.district_dashboard.presentation.event.DistrictDashboardEvent
import tech.sumato.avn.mp.feature.district_dashboard.presentation.model.DashboardStatsUiModel
import tech.sumato.avn.mp.feature.district_dashboard.presentation.model.toDistrictUiModel
import tech.sumato.avn.mp.feature.district_dashboard.presentation.state.DistrictDashboardState

@Composable
fun DistrictDashboardExpanded(
    state: DistrictDashboardState,
    onEvent: (event: DistrictDashboardEvent) -> Unit = {}
) {

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier.fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        DistrictDashboardHeader(
            modifier = Modifier.fillMaxWidth(),
            districts = if (state is DistrictDashboardState.Success) state.userDistricts.map { it.toDistrictUiModel() } else emptyList()
        )

        Spacer(modifier = Modifier.height(8.dp))

        HorizontalStatsBar(
            modifier = Modifier,
            stats = listOf(
                DashboardStatsUiModel("Total Schools", "3,248", "Across the district", 0xff0284c7),
                DashboardStatsUiModel(
                    "Internet Facility",
                    "68.5%",
                    "High-Speed FTTH / VSAT",
                    0xff00D3F3
                ),
                DashboardStatsUiModel(
                    "MDM Kitchen Sheds",
                    "91.2%",
                    "Functional Hot Meals",
                    0xff00D492
                ),
                DashboardStatsUiModel(
                    "Without Hostel",
                    "1,842",
                    "Day Scholar Institutes",
                    0xffFF6367
                ),
                DashboardStatsUiModel("Student Attendance", "91.4%", "● Live Today", 0xff00D492),
                DashboardStatsUiModel(
                    "Teacher Attendance",
                    "95.8%",
                    "● Bio-Metric Log",
                    0xff00D492
                ),
            ),
            onStatsClicked = { statsUiModel ->
                //
            }
        )

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth().height(420.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {

            DistrictDashboardMap(modifier = Modifier.weight(4f).fillMaxSize())

            DistrictDashboardSchoolCategoriesAnalytics(modifier = Modifier.weight(2f).fillMaxSize())

        }

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            OngoingProjects(modifier = Modifier.weight(1f).fillMaxWidth().fillMaxHeight())

            UpcomingEvents(modifier = Modifier.weight(1f).fillMaxWidth().fillMaxHeight())
        }


    }

}

