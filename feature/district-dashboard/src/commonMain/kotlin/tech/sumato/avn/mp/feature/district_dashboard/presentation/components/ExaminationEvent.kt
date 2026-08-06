package tech.sumato.avn.mp.feature.district_dashboard.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import tech.sumato.avn.mp.designsystem.components.AppCardBordered
import tech.sumato.avn.mp.designsystem.components.app.AppVerticalDate
import tech.sumato.avn.mp.feature.district_dashboard.presentation.model.ExaminationEventUiModel
import tech.sumato.avn.mp.feature.district_dashboard.presentation.model.getExaminationEventDateColor
import tech.sumato.avn.mp.feature.district_dashboard.presentation.model.getParsedDate


@Composable
fun ExaminationEvent(modifier: Modifier, examinationEvent: ExaminationEventUiModel) {

    val dateBgColor = getExaminationEventDateColor()
    val localDate = examinationEvent.getParsedDate()

    AppCardBordered(modifier = modifier, padding = 8.dp) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            AppVerticalDate(
                modifier = Modifier
                    .background(dateBgColor.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                    .fillMaxHeight()
                ,
                textColor = dateBgColor,
                top = localDate.month.name.slice(0..2),
                middle = localDate.day.toString(),
                bottom = localDate.year.toString()

            )



            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    examinationEvent.name,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    examinationEvent.subname,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.W400,
                    color = LocalContentColor.current.copy(alpha = 0.85f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    examinationEvent.eligibility,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.W300,
                    color = LocalContentColor.current.copy(alpha = 0.65f),
                    modifier = Modifier.wrapContentWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant,
                            RoundedCornerShape(8.dp)).padding(horizontal = 8.dp, vertical = 4.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

        }
    }


}