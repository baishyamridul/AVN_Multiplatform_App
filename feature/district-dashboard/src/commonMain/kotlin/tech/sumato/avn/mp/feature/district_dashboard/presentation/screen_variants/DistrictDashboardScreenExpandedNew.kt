package tech.sumato.avn.mp.feature.district_dashboard.presentation.screen_variants

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import tech.sumato.avn.mp.component.map.DistrictSvgMap
import tech.sumato.avn.mp.core.navigation.Route
import tech.sumato.avn.mp.designsystem.components.AppCardBordered
import tech.sumato.avn.mp.domain.common.model.DistrictModel
import tech.sumato.avn.mp.domain.districtDashboard.model.DashboardStatModel
import tech.sumato.avn.mp.domain.districtDashboard.model.DistrictDashboardData
import tech.sumato.avn.mp.feature.district_dashboard.presentation.components.DistrictDashboardHeader
import tech.sumato.avn.mp.feature.district_dashboard.presentation.components.DistrictDashboardMap
import tech.sumato.avn.mp.feature.district_dashboard.presentation.components.DistrictDashboardSchoolCategoriesAnalytics
import tech.sumato.avn.mp.feature.district_dashboard.presentation.components.HorizontalStatsBar
import tech.sumato.avn.mp.feature.district_dashboard.presentation.components.OngoingProjects
import tech.sumato.avn.mp.feature.district_dashboard.presentation.components.ProjectsStats
import tech.sumato.avn.mp.feature.district_dashboard.presentation.components.UpcomingEvents
import tech.sumato.avn.mp.feature.district_dashboard.presentation.event.DistrictDashboardEvent
import tech.sumato.avn.mp.feature.district_dashboard.presentation.model.DashboardStatsUiModel
import tech.sumato.avn.mp.feature.district_dashboard.presentation.model.DistrictUiModel
import tech.sumato.avn.mp.feature.district_dashboard.presentation.model.toDistrictUiModel
import tech.sumato.avn.mp.feature.district_dashboard.presentation.model.toOngoingProjectStatsUiModel
import tech.sumato.avn.mp.feature.district_dashboard.presentation.model.toSchoolCategoryUiModel
import tech.sumato.avn.mp.feature.district_dashboard.presentation.model.toUiModel
import tech.sumato.avn.mp.feature.district_dashboard.presentation.state.DistrictDashboardState

@Composable
fun DistrictDashboardScreenExpandedNew(
    state: DistrictDashboardState,
    onEvent: (DistrictDashboardEvent) -> Unit,
    onNavigateToSchoolDashboard: () -> Unit = {},
) {
    when (state) {
        is DistrictDashboardState.Loading -> LoadingContent()
        is DistrictDashboardState.Error -> ErrorContent(
            message = state.message,
            onRetry = { onEvent(DistrictDashboardEvent.Retry()) },
        )

        is DistrictDashboardState.Success -> ExpandedDashboardContent(
            data = state.data,
            userDistricts = state.userDistricts,
            selectedDistrictId = state.selectedDistrictId,
            isRefreshing = state.isRefreshing,
            onNavigateToSchoolDashboard = onNavigateToSchoolDashboard,
            onEvent = onEvent
        )
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(modifier = Modifier.size(48.dp))
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}

@Composable
private fun ExpandedDashboardContent(
    data: DistrictDashboardData,
    userDistricts: List<DistrictModel>,
    selectedDistrictId: Int,
    isRefreshing: Boolean,
    onNavigateToSchoolDashboard: () -> Unit,
    onEvent: (DistrictDashboardEvent) -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (isRefreshing) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp),
            )
        }

        DistrictDashboardHeader(
            modifier = Modifier.fillMaxWidth(),
            districts = userDistricts.map { it.toDistrictUiModel() },
            selectedDistrictId = selectedDistrictId,
            onDistrictSelected = { district ->
                onEvent(DistrictDashboardEvent.LoadData(district.id))
            },
            onUserClicked = {
                onEvent(DistrictDashboardEvent.Logout)
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        HorizontalStatsBar(
            modifier = Modifier,
            stats = data.stats.map { it.toStatsUiModel() },
            onStatsClicked = { },
        )

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth().height(420.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
//            DistrictDashboardMap(modifier = Modifier.weight(4f).fillMaxSize())

            AppCardBordered(modifier = Modifier.weight(4f).fillMaxSize()) {
                DistrictSvgMap(
                    modifier = Modifier.weight(4f).fillMaxSize(),
                    onDistrictClick = { svgShape ->

                    })
            }



            DistrictDashboardSchoolCategoriesAnalytics(
                modifier = Modifier.weight(2f).fillMaxSize(),
                categories = data.schoolCategoryList.map { it.toSchoolCategoryUiModel() }.toList(),
                onViewAllClicked = {
                    val districtId = userDistricts.firstOrNull { it.id == selectedDistrictId }?.id
                    onEvent(DistrictDashboardEvent.Navigate(Route.schoolDashboard(districtId)))
                },
                onCategoryClicked = { categoryId ->
                    val districtId = userDistricts.firstOrNull { it.id == selectedDistrictId }?.id
                    onEvent(
                        DistrictDashboardEvent.Navigate(
                            Route.schoolDashboard(
                                districtId,
                                categoryId
                            )
                        )
                    )
                }
            )
        }

        Spacer(Modifier.height(8.dp))

        ProjectsStats(
            modifier = Modifier.fillMaxWidth(),
            projectStats = data.projectStats.projects.map { it.toUiModel() },
            totalProjects = data.projectStats.totalProjects,
            onTotalProjectClick = {
                onEvent(DistrictDashboardEvent.Navigate(Route.PROJECT_DASHBOARD))
            }
        )


        Row(
            modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            OngoingProjects(
                modifier = Modifier.weight(1f).fillMaxWidth().fillMaxHeight(),
                projects = data.ongoingProjects.projects.map { it.toOngoingProjectStatsUiModel() },
                totalProjects = data.ongoingProjects.totalProjects
            )

            UpcomingEvents(modifier = Modifier.weight(1f).fillMaxWidth().fillMaxHeight())
        }
    }
}

private fun DashboardStatModel.toStatsUiModel(): DashboardStatsUiModel {
    val color = when (label) {
        "Internet Facility" -> 0xff00D3F3
        "MDM Kitchen Sheds" -> 0xff00D492
        "Usable Classroom" -> 0xffFF6367
        else -> 0xff0284c7
    }
    return DashboardStatsUiModel(
        label = label,
        value = value,
        supporting = description ?: "",
        valueColor = color,
    )
}
