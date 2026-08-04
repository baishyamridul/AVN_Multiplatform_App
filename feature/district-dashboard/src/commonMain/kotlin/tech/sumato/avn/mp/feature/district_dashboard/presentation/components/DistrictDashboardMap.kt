package tech.sumato.avn.mp.feature.district_dashboard.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import tech.sumato.avn.mp.component.map.MapView
import tech.sumato.avn.mp.designsystem.components.AppCardBordered


@Composable
fun DistrictDashboardMap(modifier: Modifier) {

    var loadMap by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(2000)

        loadMap = true

    }


    AppCardBordered(
        modifier = modifier,
        paddingLess = true,
    ) {
        if (loadMap)
            MapView(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp)),
                styleUrl = "",
            ) {

            }
        else
            Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface))
    }

}