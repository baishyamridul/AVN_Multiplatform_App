package tech.sumato.avn.mp.feature.district_dashboard.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.Grid
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import tech.sumato.avn.mp.feature.district_dashboard.presentation.model.ExaminationEventUiModel


@OptIn(ExperimentalGridApi::class)
@Composable
fun UpcomingEvents(modifier: Modifier) {

    AppCardBordered(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                "\uD83D\uDCC5  Upcoming Examination Schedule",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f).fillMaxWidth()
            )

            Text(
                "State Academic Calendar",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.W400,
                color = LocalContentColor.current.copy(alpha = 0.85f)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = Dp.Hairline)

        Spacer(modifier = Modifier.height(32.dp))

        Grid(config = {
            column(weight = 1.fr)
            column(weight = 1.fr)
            gap(row = 16.dp, column = 8.dp)
        }) {
            listOf(
                ExaminationEventUiModel(
                    name = "Class X & XII Board Prep",
                    subname = "Statewide Mock Assessments",
                    eligibility = "High & Higher Sec.",
                    date = "2026-09-14"
                ),
                ExaminationEventUiModel(
                    name = "Mid-Term Evaluation",
                    subname = "Classes V to VIII Syllabus",
                    eligibility = "Primary & Middle",
                    date = "2026-10-05"
                ),
                ExaminationEventUiModel(
                    name = "NAS Verification",
                    subname = "National Achievement Survey",
                    eligibility = "All Categories",
                    date = "2026-11-20"
                ),
                ExaminationEventUiModel(
                    name = "Annual Assessments",
                    subname = "Evaluation Phase Tier 1",
                    eligibility = "Primary",
                    date = "2026-12-18"
                )
            ).forEach { event ->
                ExaminationEvent(
                    modifier = Modifier.fillMaxWidth(),
                    examinationEvent = event
                )

            }
        }


    }

}