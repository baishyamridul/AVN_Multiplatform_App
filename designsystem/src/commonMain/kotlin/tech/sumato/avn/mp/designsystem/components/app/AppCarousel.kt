package tech.sumato.avn.mp.designsystem.components.app

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import org.jetbrains.compose.resources.painterResource
import kotlin.math.absoluteValue


@Composable
fun AppCarousel(
    modifier: Modifier,
    images: List<String>,
    caption: (index: Int) -> String = { "" },
    onImageClick: ((index: Int) -> Unit)? = null,
) {
    val pagerState =
        rememberPagerState(pageCount = { images.size }, initialPage = if (images.size > 1) 1 else 0)

    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 48.dp), // Leaves a peak of adjacent items
        pageSpacing = 16.dp,
        modifier = modifier
    ) { pageIndex ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    // Calculate visual offset of current page from center screen
                    val pageOffset = (
                            (pagerState.currentPage - pageIndex) + pagerState.currentPageOffsetFraction
                            ).absoluteValue

                    // Apply smooth scale animations to off-center items
                    alpha = lerp(
                        start = 0.5f,
                        stop = 1.0f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )
                    scaleY = lerp(
                        start = 0.8f,
                        stop = 1.0f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )
                }
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable(onClick = { onImageClick?.invoke(pageIndex) })
            ) {
                AsyncImage(
                    model = images[pageIndex],
                    "",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxWidth()
//                        .aspectRatio(16f / 9f)
                        .clip(RoundedCornerShape(16.dp))
                )
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
////                        .clickable(onClick = { onImageClick?.invoke(pageIndex) })
//                )
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceDim.copy(alpha = 0.75f))
                        .align(
                            Alignment.BottomCenter
                        )
                ) {
                    Text(
                        caption(pageIndex),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                        textAlign = TextAlign.Center
                    )
                }

            }


        }
    }
}