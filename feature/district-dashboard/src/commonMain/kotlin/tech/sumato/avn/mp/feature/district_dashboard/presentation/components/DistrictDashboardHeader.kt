package tech.sumato.avn.mp.feature.district_dashboard.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import tech.sumato.avn.mp.designsystem.theme.geistMonoFontFamily


@Composable
fun DistrictDashboardHeader(
    modifier: Modifier,
) {

    Row(modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier.size(50.dp).background(Color.Yellow, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("AVN", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier
                .weight(1f).fillMaxWidth()
        ) {
            Text("ARUNACHAL VIDYA NIDHI", style = MaterialTheme.typography.titleMedium)
            Text(
                "शिक्षित अरुणाचल, विकसित अरुणाचल • CM Executive Dashboard",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.W500
            )
        }

        Row(modifier = Modifier, horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.border(
                    1.dp, color = MaterialTheme.colorScheme.surfaceBright,
                    RoundedCornerShape(8.dp)
                )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text("All District", style = MaterialTheme.typography.bodyLarge)
            }


            Box(modifier = Modifier) {
                Text("18 July 2026 5:54 PM", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.W500)
            }

        }


    }

}