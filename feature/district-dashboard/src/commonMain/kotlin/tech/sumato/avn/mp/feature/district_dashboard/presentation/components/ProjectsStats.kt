package tech.sumato.avn.mp.feature.district_dashboard.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import tech.sumato.avn.mp.designsystem.components.AppCardBordered
import tech.sumato.avn.mp.feature.district_dashboard.presentation.model.DistrictDashboardProjectStatsUiModel
import kotlin.math.roundToInt


@Composable
fun ProjectsStats(
    modifier: Modifier,
    projectStats: List<DistrictDashboardProjectStatsUiModel>,
    totalProjects: Int
) {

    AppCardBordered(modifier = modifier) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "\uD83C\uDFD7\uFE0F Ongoing Infrastructure Projects & Completion Status",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f).fillMaxWidth()
            )

            Surface(
                shape = RoundedCornerShape(50),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
            ) {
                Text(
                    "$totalProjects total projects",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    color = LocalContentColor.current.copy(alpha = 0.85f),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        BoxWithConstraints(modifier = Modifier) {
            val spacing = 16.dp
            val columns = when {
                maxWidth >= 1000.dp -> 4
                maxWidth >= 640.dp -> 3
                maxWidth >= 400.dp -> 2
                else -> 1
            }
            val itemWidth = ((maxWidth - spacing * (columns - 1)) / columns).coerceAtLeast(0.dp)
            val cardHeight = 148.dp

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                maxItemsInEachRow = columns,
                horizontalArrangement = Arrangement.spacedBy(spacing),
                verticalArrangement = Arrangement.spacedBy(spacing)
            ) {

                projectStats.forEach { stats ->
                    val percent = stats.completedPercent.coerceIn(0f, 100f)
                    val accent = completionColor(percent)

                    AppCardBordered(modifier = Modifier.width(itemWidth).height(cardHeight)) {
                        Column(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {

                            Row(
                                modifier = Modifier.weight(1f).fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    stats.categoryName,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.weight(1f)
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    "${percent.roundToInt()}%",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = accent
                                )
                            }

                            LinearProgressIndicator(
                                modifier = Modifier.fillMaxWidth().height(8.dp),
                                progress = { percent / 100f },
                                color = accent,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                                strokeCap = StrokeCap.Round
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                StatChip(
                                    modifier = Modifier.weight(1f),
                                    count = stats.totalCompleted,
                                    label = "completed",
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                StatChip(
                                    modifier = Modifier.weight(1f),
                                    count = stats.totalOngoing,
                                    label = "ongoing",
                                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                StatChip(
                                    modifier = Modifier.weight(1f),
                                    count = stats.total,
                                    label = "total",
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatChip(
    modifier: Modifier,
    count: Int,
    label: String,
    containerColor: Color,
    contentColor: Color
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = containerColor
    ) {
        Text(
            "$count $label",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = contentColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp)
        )
    }
}

private fun completionColor(percent: Float): Color = when {
    percent >= 75f -> Color(0xFF2E7D32)
    percent >= 40f -> Color(0xFFEF6C00)
    else -> Color(0xFFC62828)
}
