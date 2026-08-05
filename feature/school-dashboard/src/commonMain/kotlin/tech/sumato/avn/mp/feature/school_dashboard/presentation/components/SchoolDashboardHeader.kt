package tech.sumato.avn.mp.feature.school_dashboard.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import tech.sumato.avn.mp.designsystem.components.app.AppDateTime
import tech.sumato.avn.mp.designsystem.components.app.AppHeaderCustom
import tech.sumato.avn.mp.designsystem.components.app.fields.AppDropDownBasic

@Composable
fun SchoolDashboardHeader(
    modifier: Modifier,
    districts: List<String>,
    selectedDistrict: String?,
    onDistrictSelected: (String?) -> Unit,
    onBack: () -> Unit,
) {

    AppHeaderCustom(modifier = modifier, leading = {
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Default.ArrowBack, "", modifier = Modifier)
        }
        Spacer(modifier = Modifier.width(12.dp))
    }) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {

            val options = listOf(ALL_DISTRICT_LABEL) + districts

            AppDropDownBasic(
                modifier = Modifier
                    .width(168.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        1.dp, color = MaterialTheme.colorScheme.outline,
                        RoundedCornerShape(8.dp)
                    ),
                options = options,
                labelTransformer = { it },
                onSelected = { option ->
                    onDistrictSelected(
                        if (option == ALL_DISTRICT_LABEL) null else option
                    )
                },
                selected = selectedDistrict ?: ALL_DISTRICT_LABEL,
            ) { currentOption ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        currentOption ?: ALL_DISTRICT_LABEL,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Show options")
                }
            }

            Spacer(modifier = Modifier.width(16.dp))


            AppDateTime(modifier = Modifier)
        }
    }

}

private const val ALL_DISTRICT_LABEL = "All Districts"
