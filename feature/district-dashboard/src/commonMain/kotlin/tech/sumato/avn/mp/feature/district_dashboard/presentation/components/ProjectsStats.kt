package tech.sumato.avn.mp.feature.district_dashboard.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.sumato.avn.mp.core.navigation.Route
import tech.sumato.avn.mp.designsystem.FormFactor
import tech.sumato.avn.mp.designsystem.LocalFormFactor
import tech.sumato.avn.mp.designsystem.components.AppCardBordered
import tech.sumato.avn.mp.designsystem.components.app.AppChip
import tech.sumato.avn.mp.designsystem.theme.geistMonoFontFamily
import tech.sumato.avn.mp.feature.district_dashboard.presentation.event.DistrictDashboardEvent
import tech.sumato.avn.mp.feature.district_dashboard.presentation.model.DistrictDashboardProjectStatsUiModel
import kotlin.math.roundToInt


@Composable
fun ProjectsStats(
    modifier: Modifier,
    projectStats: List<DistrictDashboardProjectStatsUiModel>,
    totalProjects: Int,
    onTotalProjectClick: () -> Unit = {}
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


            AppChip(
                modifier = Modifier,
                onClick = {
                    onTotalProjectClick()
                }
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

        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val spacing = 16.dp

            val columns = when (LocalFormFactor.current) {
                FormFactor.Compact -> 2
                FormFactor.Medium -> 3
                FormFactor.Expanded -> 4
            }

            val itemWidth = ((maxWidth - spacing * (columns - 1)) / columns).coerceAtLeast(0.dp)

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                maxItemsInEachRow = columns,
                horizontalArrangement = Arrangement.spacedBy(spacing),
                verticalArrangement = Arrangement.spacedBy(spacing)
            ) {

                projectStats.forEach { stats ->
                    val percent = stats.completedPercent.coerceIn(0f, 100f)
                    val accent = completionColor(percent)

                    ProjectStatsCategoryCard(
                        modifier = Modifier.width(itemWidth).height(136.dp),
                        accent = accent,
                        stats = stats
                    )

                    /*AppCardBordered(
                        modifier = Modifier.width(itemWidth).height(128.dp),
                        border = BorderStroke(1.dp, accent.copy(alpha = 0.35f)),
                        containerColor = lerp(MaterialTheme.colorScheme.surface, accent, 0.06f)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {

                            Column(modifier = Modifier.weight(1f).fillMaxHeight()) {

                                Row(
                                    modifier = Modifier.weight(1f).fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Text(
                                        stats.categoryName,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.weight(1f),
                                        minLines = 2,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Surface(
                                        shape = RoundedCornerShape(50),
                                        color = accent.copy(alpha = 0.12f)
                                    ) {
                                        Text(
                                            "${percent.roundToInt()}%",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = accent,
                                            modifier = Modifier.padding(
                                                horizontal = 6.dp,
                                                vertical = 2.dp
                                            )
                                        )
                                    }
                                }

                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 8.dp, vertical = 6.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            "projects completed",
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.Normal,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(
                                                alpha = 0.75f
                                            )
                                        )

                                        Text(
                                            "${stats.totalCompleted} of ${stats.total}",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                LinearProgressIndicator(
                                    modifier = Modifier.fillMaxWidth().height(8.dp),
                                    progress = { percent / 100f },
                                    color = accent,
                                    trackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                                    strokeCap = StrokeCap.Round
                                )
                            }
                        }
                    }*/
                }
            }
        }
    }
}

private fun completionColor(percent: Float): Color = when {
    percent >= 75f -> Color(0xFF2E7D32)
    percent >= 40f -> Color(0xFFEF6C00)
    else -> Color(0xFFC62828)
}


@Composable
fun ProjectStatsCategoryCard(
    modifier: Modifier, accent: Color, stats: DistrictDashboardProjectStatsUiModel
) {

    val percent = stats.completedPercent.coerceIn(0f, 100f)

    AppCardBordered(
        modifier = modifier,
        border = BorderStroke(1.dp, accent.copy(alpha = 0.35f)),
        containerColor = lerp(MaterialTheme.colorScheme.surface, accent, 0.06f)
    ) {

        Box(modifier = Modifier.fillMaxSize()) {

            Column(modifier = Modifier.fillMaxHeight()) {
                Text(
                    stats.categoryName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier,
                    minLines = 2,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    val projectCounts = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontFamily = geistMonoFontFamily(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                            )
                        ) {
                            append("${stats.totalCompleted}")
                        }
                        withStyle(
                            style = SpanStyle(
                                fontFamily = geistMonoFontFamily(),
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp
                            )
                        ) {
                            append("/${stats.total} completed")
                        }
                    }

                    Text(projectCounts)

                }

            }


            Box(modifier = Modifier.size(62.dp).align(Alignment.BottomEnd)) {
                CircularProgressIndicator(
                    modifier = Modifier.fillMaxSize(),
                    strokeWidth = 6.dp,
                    progress = { percent / 100f },
                    color = accent,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                    strokeCap = StrokeCap.Round
                )

                Text(
                    "${percent.roundToInt()}%",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.align(
                        Alignment.Center
                    ),
                    color = accent
                )
            }
        }


    }
}
