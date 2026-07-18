package tech.sumato.avn.mp.feature.district_dashboard.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun DistrictDashboardScreen(
    state: DistrictDashboardState,
    onEvent: (DistrictDashboardEvent) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {

        TopAppBar(
            title = {
                Text(
                    "District Dashboard",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            navigationIcon = {
                IconButton(onClick = {}) {
                    Icon(Icons.AutoMirrored.Default.ArrowBack, "")
                }
            }
        )

        Text(
            "DistrictDashboard", style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}
