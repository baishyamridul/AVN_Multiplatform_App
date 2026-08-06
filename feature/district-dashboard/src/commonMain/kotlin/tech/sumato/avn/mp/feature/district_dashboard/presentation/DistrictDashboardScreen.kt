package tech.sumato.avn.mp.feature.district_dashboard.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import tech.sumato.avn.mp.designsystem.FormFactor
import tech.sumato.avn.mp.designsystem.LocalFormFactor
import tech.sumato.avn.mp.designsystem.components.AppCard
import tech.sumato.avn.mp.designsystem.components.ScreenHeader
import tech.sumato.avn.mp.designsystem.components.StatCard
import tech.sumato.avn.mp.feature.district_dashboard.presentation.event.DistrictDashboardEvent
import tech.sumato.avn.mp.feature.district_dashboard.presentation.screen_variants.DistrictDashboardCompact
import tech.sumato.avn.mp.feature.district_dashboard.presentation.screen_variants.DistrictDashboardMedium
import tech.sumato.avn.mp.feature.district_dashboard.presentation.screen_variants.DistrictDashboardScreenExpandedNew
import tech.sumato.avn.mp.feature.district_dashboard.presentation.state.DistrictDashboardState

@Composable
fun DistrictDashboardScreen(
    state: DistrictDashboardState,
    onEvent: (DistrictDashboardEvent) -> Unit,
) {
    val formFactor = LocalFormFactor.current

    when (formFactor) {
        FormFactor.Compact -> DistrictDashboardCompact()
        FormFactor.Medium -> DistrictDashboardMedium()
        FormFactor.Expanded -> DistrictDashboardScreenExpandedNew(state, onEvent)
    }
}

@Composable
fun DistrictStatsGrid(columns: Int) {
    val stats = listOf(
        "Total Districts" to "24",
        "Active" to "18",
        "Pending Review" to "4",
        "Issues" to "2",
    )

    val rows = stats.chunked(columns)
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        rows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                row.forEach { (label, value) ->
                    StatCard(
                        label = label,
                        value = value,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@Composable
fun DistrictList() {
    val districts = listOf(
        "North District" to "256 reports",
        "South District" to "189 reports",
        "East District" to "312 reports",
        "West District" to "145 reports",
        "Central District" to "198 reports",
    )

    AppCard {
        Text(
            "Districts",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 12.dp),
        )
        districts.forEach { (name, reports) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(name, style = MaterialTheme.typography.bodyMedium)
                Text(reports, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
