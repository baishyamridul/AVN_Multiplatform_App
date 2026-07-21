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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import tech.sumato.avn.mp.component.map.MapView
import tech.sumato.avn.mp.designsystem.components.AppCardBordered
import tech.sumato.avn.mp.designsystem.components.school.AppSchoolCategoryCard
import tech.sumato.avn.mp.feature.district_dashboard.presentation.components.DistrictDashboardHeader
import tech.sumato.avn.mp.feature.district_dashboard.presentation.components.StatsCard
import tech.sumato.avn.mp.feature.district_dashboard.presentation.model.DashboardStatsUiModel
import tech.sumato.avn.mp.feature.district_dashboard.presentation.model.SchoolCategoryUiModel

@Composable
fun DistrictDashboardExpanded() {
    val scrollState = rememberScrollState()
    var isMapTouched by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier.fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        DistrictDashboardHeader(modifier = Modifier.fillMaxWidth())

        Row(
            modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            listOf(
                DashboardStatsUiModel("Total Schools", "3,248", "Across the district", 0xfff4f4f4),
                DashboardStatsUiModel("Internet Facility", "68.5%", "High-Speed FTTH / VSAT", 0xff00D3F3),
                DashboardStatsUiModel("MDM Kitchen Sheds", "91.2%", "Functional Hot Meals", 0xff00D492),
                DashboardStatsUiModel("Without Hostel", "1,842", "Day Scholar Institutes", 0xffFF6367),
                DashboardStatsUiModel("Student Attendance", "91.4%", "● Live Today", 0xff00D492),
                DashboardStatsUiModel("Teacher Attendance", "95.8%", "● Bio-Metric Log", 0xff00D492),
            ).forEach { it ->
                StatsCard(
                    modifier = Modifier.weight(1f).fillMaxWidth().fillMaxHeight(),
                    statsDto = it,
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth().height(380.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            AppCardBordered(
                modifier = Modifier.weight(4f).fillMaxSize(),
                paddingLess = true,
            ) {
                MapView(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp))
                    ,
                    styleUrl = "",
                )
            }

            AppCardBordered(
                modifier = Modifier.weight(2f).fillMaxSize(),
            ) {
                Text(
                    "\uD83C\uDFEB Categorized Distribution Analytics",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(Modifier.height(24.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(
                        listOf(
                            SchoolCategoryUiModel("1", "Primary School", "I - V", "1,420"),
                            SchoolCategoryUiModel("2", "Middle Schools", "VI - VIII", "984"),
                            SchoolCategoryUiModel("3", "High Schools", "IX - X", "512"),
                            SchoolCategoryUiModel("4", "Higher Secondary Schools", "XI - XII", "332"),
                        ),
                    ) { item ->
                        AppSchoolCategoryCard(
                            modifier = Modifier.fillMaxWidth(),
                            name = item.label,
                            schoolCount = item.schoolCount,
                            classRange = item.classRange,
                        )
                    }
                }
            }
        }
    }
}
