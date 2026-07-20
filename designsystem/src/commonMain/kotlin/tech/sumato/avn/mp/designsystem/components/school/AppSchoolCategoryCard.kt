package tech.sumato.avn.mp.designsystem.components.school

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import tech.sumato.avn.mp.designsystem.components.AppCard
import tech.sumato.avn.mp.designsystem.components.AppCardBordered


@Composable
fun AppSchoolCategoryCard(
    modifier: Modifier,
    name: String,
    schoolCount: String,
    classRange: String,
    onClick: (() -> Unit)? = null,
) {

    AppCardBordered(
        modifier = modifier,
    ) {


        Row {
            Text(
                name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f).fillMaxWidth()
            )
            Text(
                schoolCount,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            "Class $classRange",
            style = MaterialTheme.typography.bodySmall,
        )


    }

}