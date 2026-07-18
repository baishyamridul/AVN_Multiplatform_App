package tech.sumato.avn.mp.feature.district_dashboard.presentation.screen_variants

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import tech.sumato.avn.mp.designsystem.components.ScreenHeader
import tech.sumato.avn.mp.feature.district_dashboard.presentation.DistrictList
import tech.sumato.avn.mp.feature.district_dashboard.presentation.DistrictStatsGrid
import tech.sumato.avn.mp.feature.district_dashboard.presentation.components.DistrictDashboardHeader

@Composable
fun DistrictDashboardExpanded() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 8.dp)
        ,
    ) {

        DistrictDashboardHeader(modifier = Modifier.fillMaxWidth())

        ScreenHeader(
            title = "District Dashboard",
            subtitle = "Overview of all districts.",
        )
        Spacer(Modifier.height(24.dp))
        DistrictStatsGrid(columns = 4)
        Spacer(Modifier.height(24.dp))
        DistrictList()
        Spacer(Modifier.height(16.dp))
    }
}