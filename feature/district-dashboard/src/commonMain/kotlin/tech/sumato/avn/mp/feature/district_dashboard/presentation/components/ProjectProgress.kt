package tech.sumato.avn.mp.feature.district_dashboard.presentation.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import tech.sumato.avn.mp.feature.district_dashboard.presentation.model.OngoingProjectStatsUiModel
import tech.sumato.avn.mp.feature.district_dashboard.presentation.model.progressColors
import kotlin.math.roundToInt


@Composable
fun ProjectProgress(project: OngoingProjectStatsUiModel) {

    val darkTheme = isSystemInDarkTheme()

    val progressColor = project.progressColors(darkTheme)




//    AppCard(modifier = Modifier.fillMaxWidth()) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(0.dp)) {

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                project.name,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )

            Text(
                "${project.progress.roundToInt()}% completed",
                style = MaterialTheme.typography.bodySmall,
                color = progressColor
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth().height(6.dp),
            progress = { project.progress.toFloat() / 100 },
            color = progressColor,
            strokeCap = StrokeCap.Round,
            gapSize = 0.dp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            "${project.region.replaceFirstChar { it.uppercase() }} Region",
            style = MaterialTheme.typography.bodySmall,
            color = LocalContentColor.current.copy(alpha = 0.75f)
        )

    }
//    }

}