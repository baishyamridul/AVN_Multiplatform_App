package tech.sumato.avn.mp.feature.school_dashboard.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tech.sumato.avn.mp.designsystem.components.AppCardBordered
import tech.sumato.avn.mp.designsystem.components.ShimmerBox
import tech.sumato.avn.mp.designsystem.components.ShimmerTextLines


@Composable
fun SchoolListItemShimmer(
    modifier: Modifier = Modifier,
) {
    AppCardBordered(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                ShimmerBox(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(16.dp),
                )
                Spacer(modifier = Modifier.height(8.dp))
                ShimmerTextLines(
                    lines = 2,
                    lineHeight = 12.dp,
                    spacing = 6.dp,
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            ShimmerBox(
                modifier = Modifier.size(24.dp),
                shape = RoundedCornerShape(50),
            )
        }
    }
}


@Composable
fun SchoolListShimmer(
    modifier: Modifier = Modifier,
    itemCount: Int = 6,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(itemCount) {
            SchoolListItemShimmer()
        }
    }
}


@Composable
fun MapPanelShimmer(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(2.dp),
    ) {
        ShimmerBox(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(10.dp),
        )
    }
}


@Composable
fun SchoolDetailsShimmer(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                ShimmerBox(
                    modifier = Modifier
                        .width(120.dp)
                        .height(24.dp),
                    shape = RoundedCornerShape(50),
                )
                ShimmerBox(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(18.dp),
                )
                ShimmerTextLines(
                    lines = 2,
                    lineHeight = 12.dp,
                    spacing = 6.dp,
                )
            }
            ShimmerBox(
                modifier = Modifier.size(80.dp),
                shape = RoundedCornerShape(12.dp),
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        ShimmerBox(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            shape = RoundedCornerShape(12.dp),
        )

        repeat(3) {
            AppCardBordered(modifier = Modifier.fillMaxWidth()) {
                ShimmerTextLines(
                    lines = 2,
                    lineHeight = 14.dp,
                    spacing = 8.dp,
                )
            }
        }
    }
}
