package tech.sumato.avn.mp.feature.district_dashboard.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tech.sumato.avn.mp.designsystem.components.AppCard
import tech.sumato.avn.mp.feature.district_dashboard.presentation.model.DashboardStatsUiModel


@Composable
fun StatsCard(
    modifier: Modifier,
    statsDto: DashboardStatsUiModel
) {

    AppCard(
        modifier = modifier,
        border = BorderStroke(Dp.Hairline, MaterialTheme.colorScheme.outline)
    ) {
        Text(
            statsDto.label.uppercase(),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.W500
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            statsDto.value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color(statsDto.valueColor)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            statsDto.supporting,
            style = MaterialTheme.typography.bodySmall,
            color = Color(statsDto.valueColor)
        )
    }

}