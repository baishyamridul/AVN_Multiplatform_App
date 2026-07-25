package tech.sumato.avn.mp.feature.district_dashboard.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tech.sumato.avn.mp.feature.district_dashboard.presentation.model.DashboardStatsUiModel


@Composable
fun HorizontalStatsBar(
    modifier: Modifier,
    stats: List<DashboardStatsUiModel>,
    onStatsClicked: (stats: DashboardStatsUiModel) -> Unit = {}
) {

    Row(
        modifier = modifier.fillMaxWidth().height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        stats.forEach { it ->
            StatsCard(
                modifier = Modifier.weight(1f).fillMaxWidth().fillMaxHeight(),
                statsModel = it,
                onClick = onStatsClicked
            )
        }
    }

}