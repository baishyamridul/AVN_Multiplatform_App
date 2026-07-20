package tech.sumato.avn.mp.feature.district_dashboard.presentation.screen_variants

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tech.sumato.avn.mp.component.map.MapView
import tech.sumato.avn.mp.designsystem.components.AppCard
import tech.sumato.avn.mp.designsystem.components.AppCardBordered
import tech.sumato.avn.mp.designsystem.components.school.AppSchoolCategoryCard
import tech.sumato.avn.mp.feature.district_dashboard.presentation.components.DistrictDashboardHeader
import tech.sumato.avn.mp.feature.district_dashboard.presentation.components.StatsCard
import tech.sumato.avn.mp.feature.district_dashboard.presentation.model.DashboardStatsUiModel
import tech.sumato.avn.mp.feature.district_dashboard.presentation.model.SchoolCategoryUiModel

@Composable
fun DistrictDashboardExpanded() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        DistrictDashboardHeader(
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                listOf(
                    DashboardStatsUiModel(
                        label = "Total Schools",
                        value = "3,248",
                        supporting = "Across the district",
                        valueColor = 0xfff4f4f4
                    ), DashboardStatsUiModel(
                        label = "Internet Facility",
                        value = "68.5%",
                        supporting = "High-Speed FTTH / VSAT",
                        valueColor = 0xff00D3F3
                    ), DashboardStatsUiModel(
                        label = "MDM Kitchen Sheds",
                        value = "91.2%",
                        supporting = "Functional Hot Meals",
                        valueColor = 0xff00D492
                    ), DashboardStatsUiModel(
                        label = "Without Hostel",
                        value = "1,842",
                        supporting = "Day Scholar Institutes",
                        valueColor = 0xffFF6367
                    ), DashboardStatsUiModel(
                        label = "Student Attendance",
                        value = "91.4%",
                        supporting = "● Live Today",
                        valueColor = 0xff00D492
                    ), DashboardStatsUiModel(
                        label = "Teacher Attendance",
                        value = "95.8%",
                        supporting = "● Bio-Metric Log",
                        valueColor = 0xff00D492
                    )
                ).forEach { it ->
                    StatsCard(
                        modifier = Modifier.weight(1f).fillMaxWidth().wrapContentHeight(),
                        statsDto = it
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth().height(380.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {


                AppCardBordered(
                    modifier = Modifier.weight(4f).fillMaxSize(),
                    paddingLess = true,
                ) {
                    MapView(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp)),
                        styleUrl = ""
                    )
                }

                AppCardBordered(
                    modifier = Modifier.weight(2f).fillMaxSize(),
                ) {
                    Text(
                        "\uD83C\uDFEB Categorized Distribution Analytics",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            listOf(
                                SchoolCategoryUiModel(
                                    id = "1",
                                    label = "Primary School",
                                    classRange = "I - V",
                                    schoolCount = "1,420"
                                ),
                                SchoolCategoryUiModel(
                                    id = "2",
                                    label = "Middle Schools",
                                    classRange = "VI - VIII",
                                    schoolCount = "984"
                                ),
                                SchoolCategoryUiModel(
                                    id = "3",
                                    label = "High Schools",
                                    classRange = "IX - X",
                                    schoolCount = "512"
                                ),
                                SchoolCategoryUiModel(
                                    id = "4",
                                    label = "Higher Secondary Schools",
                                    classRange = "XI - XII",
                                    schoolCount = "332"
                                ),
                            )
                        ) { item ->
                            AppSchoolCategoryCard(
                                modifier = Modifier.fillMaxWidth(),
                                name = item.label,
                                schoolCount = item.schoolCount,
                                classRange = item.classRange
                            )
                        }
                    }

                }
            }


//        ScreenHeader(
//            title = "District Dashboard",
//            subtitle = "Overview of all districts.",
//        )
//        Spacer(Modifier.height(24.dp))
//        DistrictStatsGrid(columns = 4)
//        Spacer(Modifier.height(24.dp))
//        DistrictList()
//        Spacer(Modifier.height(16.dp))
        }
    }
}