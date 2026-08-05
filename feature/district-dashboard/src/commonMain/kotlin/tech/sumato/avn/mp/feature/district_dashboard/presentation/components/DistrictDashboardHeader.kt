package tech.sumato.avn.mp.feature.district_dashboard.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import tech.sumato.avn.mp.designsystem.components.app.AppDateTime
import tech.sumato.avn.mp.designsystem.components.app.AppHeaderCustom
import tech.sumato.avn.mp.designsystem.components.app.fields.AppDropDownBasic
import tech.sumato.avn.mp.feature.district_dashboard.presentation.model.DistrictUiModel


@Composable
fun DistrictDashboardHeader(
    modifier: Modifier,
    districts: List<DistrictUiModel> = emptyList(),
    selectedDistrictId: Int = -1,
    onDistrictSelected: (district: DistrictUiModel) -> Unit = {},
    onUserClicked: () -> Unit = {},
) {

    LaunchedEffect(selectedDistrictId) {
        println("mridx: $selectedDistrictId")
    }

    val options = buildList {
        add(DistrictUiModel(-1, "All District"))
        addAll(districts)
    }

    AppHeaderCustom(modifier = modifier, leading = {}) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {

            AppDropDownBasic(
                modifier = Modifier
                    .width(168.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        1.dp, color = MaterialTheme.colorScheme.outline,
                        RoundedCornerShape(8.dp)
                    ),
                options = options,
                labelTransformer = { it.toString() },
                onSelected = { it ->
                    onDistrictSelected(it)
                },
                selected = options.firstOrNull { it.id == selectedDistrictId } ?: options.first(),
            ) { currentOption ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        currentOption?.name ?: "All District",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Show options")
                }
            }

            Spacer(modifier = Modifier.width(16.dp))


            AppDateTime(modifier = Modifier)

            IconButton(onClick = onUserClicked) {
                Icon(Icons.Default.Person, "")
            }

        }
    }


}