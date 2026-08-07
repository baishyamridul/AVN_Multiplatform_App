package tech.sumato.avn.mp.designsystem.components.app

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import org.jetbrains.compose.resources.painterResource
import kotlin.math.absoluteValue


@Composable
fun AppCarousel(modifier: Modifier, images: List<String>) {
    val pagerState = rememberPagerState(pageCount = { images.size })

    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 48.dp), // Leaves a peak of adjacent items
        pageSpacing = 16.dp,
        modifier = Modifier.height(200.dp)
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


            AsyncImage(
                model = images[pageIndex],
                "",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(16.dp))
            )

//            Image(
//                painter = painterResource(images[pageIndex]),
//                contentDescription = null,
//                contentScale = ContentScale.Crop,
//                modifier = Modifier.fillMaxSize()
//            )
        }
    }
}