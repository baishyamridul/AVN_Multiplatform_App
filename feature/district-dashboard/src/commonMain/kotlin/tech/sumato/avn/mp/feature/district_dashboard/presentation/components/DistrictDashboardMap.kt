package tech.sumato.avn.mp.feature.district_dashboard.presentation.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import tech.sumato.avn.mp.component.map.MapView
import tech.sumato.avn.mp.designsystem.components.AppCardBordered


@Composable
fun DistrictDashboardMap(modifier: Modifier) {

    AppCardBordered(
        modifier = modifier,
        paddingLess = true,
    ) {
        MapView(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(12.dp)),
            styleUrl = "",
        )
    }

}