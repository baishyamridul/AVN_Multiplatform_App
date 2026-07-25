package tech.sumato.avn.mp.feature.district_dashboard.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import avnmultiplatformapp.designsystem.generated.resources.app_logo
import org.jetbrains.compose.resources.painterResource
import tech.sumato.avn.mp.designsystem.components.app.AppDateTime
import tech.sumato.avn.mp.designsystem.components.app.fields.AppDropDownBasic
import tech.sumato.avn.mp.designsystem.components.app.fields.AppDropdown
import tech.sumato.avn.mp.designsystem.components.app.fields.AppSimpleDropDown
import avnmultiplatformapp.designsystem.generated.resources.Res as DesignSystemRes


@Composable
fun DistrictDashboardHeader(
    modifier: Modifier,
) {

    Column {


        Row(
            modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier.size(48.dp)
//                    .background(Color.Yellow, CircleShape),
                ,
                contentAlignment = Alignment.Center
            ) {

                Image(
                    painter = painterResource(DesignSystemRes.drawable.app_logo),
                    "",
                    modifier = Modifier.width(52.dp),
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .weight(1f).fillMaxWidth()
            ) {
                Text(
                    "ARUNACHAL VIDYA NIDHI",
                    style = MaterialTheme.typography.headlineSmall,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    "शिक्षित अरुणाचल, विकसित अरुणाचल",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.W500
                )
            }


            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
//                Row(
//                    modifier = Modifier.border(
//                        1.dp, color = MaterialTheme.colorScheme.outline,
//                        RoundedCornerShape(8.dp)
//                    )
//                        .padding(horizontal = 12.dp, vertical = 6.dp)
//                ) {
//                    Text("All District", style = MaterialTheme.typography.bodySmall)
//
//                    AppSimpleDropDown()
//
//                }

                val options = listOf("All District", "Kamrup", "Kamrup Metro", "Palashbari")

                AppDropDownBasic(
                    modifier = Modifier
                        .width(168.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            1.dp, color = MaterialTheme.colorScheme.outline,
                            RoundedCornerShape(8.dp)
                        ),
                    options = options,
                    onSelected = { it -> },
                    selected = options.first(),
                ) { currentOption ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            currentOption ?: "All District",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Show options")
                    }
                }


                AppDateTime(modifier = Modifier)

            }


        }

        Spacer(modifier = Modifier.height(12.dp))

        HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = Dp.Hairline)

    }


}