package tech.sumato.avn.mp.feature.district_dashboard.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tech.sumato.avn.mp.designsystem.components.AppCardBordered
import tech.sumato.avn.mp.feature.district_dashboard.presentation.model.OngoingProjectStatsUiModel


@Composable
fun OngoingProjects(modifier: Modifier) {


    AppCardBordered(modifier = modifier) {

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            Text(
                "\uD83C\uDFD7\uFE0F Ongoing Infrastructure Projects & Completion Status",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f).fillMaxWidth()
            )

            Text(
                "418 Total Blocks",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.W400,
                color = LocalContentColor.current.copy(alpha = 0.85f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = Dp.Hairline)

        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(32.dp)) {
            listOf(
                OngoingProjectStatsUiModel(
                    id = "1",
                    name = "Deployment of 45 Virtual Smart Labs",
                    region = "Tawang",
                    progress = 75.0
                ),
                OngoingProjectStatsUiModel(
                    id = "2",
                    name = "WASH Hygiene Infrastructure Upgrades",
                    region = "Tawang",
                    progress = 90.0
                ),
                OngoingProjectStatsUiModel(
                    id = "3",
                    name = "Solar Micro-Grid Integrations",
                    region = "Lower Subansiri",
                    progress = 45.0
                ),
            ).forEach {
                ProjectProgress(project = it)
            }
        }


    }

}