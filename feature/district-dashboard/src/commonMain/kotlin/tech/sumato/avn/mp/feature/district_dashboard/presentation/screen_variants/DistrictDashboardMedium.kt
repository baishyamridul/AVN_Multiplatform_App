package tech.sumato.avn.mp.feature.district_dashboard.presentation.screen_variants

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tech.sumato.avn.mp.designsystem.components.ScreenHeader
import tech.sumato.avn.mp.feature.district_dashboard.presentation.DistrictList
import tech.sumato.avn.mp.feature.district_dashboard.presentation.DistrictStatsGrid

@Composable
fun DistrictDashboardMedium() {
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(end = 12.dp),
        ) {
            ScreenHeader(
                title = "District Dashboard",
                subtitle = "Overview of all districts.",
            )
            Spacer(Modifier.height(20.dp))
            DistrictStatsGrid(columns = 2)
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(start = 12.dp),
        ) {
            DistrictList()
        }
    }
}