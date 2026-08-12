package tech.sumato.avn.mp.component.image_viewer

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil3.compose.AsyncImage
import kotlinx.coroutines.launch
import tech.sumato.avn.mp.component.image360.PanoImageViewer

private const val MaxZoom = 5f

@Composable
fun ImageModal(
    source: ImageSource?,
    onDismissRequest: () -> Unit,
) {
    if (source == null) return

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
        ),
    ) {
        val caption = when (source) {
            is ImageSource.Photo -> source.caption
            is ImageSource.Pano -> source.caption
        }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Black,
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                when (source) {
                    is ImageSource.Photo -> ZoomablePhoto(
                        url = source.url,
                        contentDescription = source.caption,
                        onTap = onDismissRequest,
                    )

                    is ImageSource.Pano -> PanoImageViewer(configUrl = source.configUrl)
                }

                Surface(
                    onClick = onDismissRequest,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .statusBarsPadding()
                        .padding(12.dp),
                    shape = CircleShape,
                    color = Color.Black.copy(alpha = 0.5f),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close",
                        tint = Color.White,
                        modifier = Modifier.padding(8.dp),
                    )
                }

                caption?.let { caption ->
                    Text(
                        text = caption,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .navigationBarsPadding()
                            .fillMaxWidth()
                            .background(Color.Black.copy(alpha = 0.5f))
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun ZoomablePhoto(
    url: String,
    contentDescription: String?,
    onTap: () -> Unit,
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var containerSize by remember { mutableStateOf(IntSize.Zero) }
    val scope = rememberCoroutineScope()

    val transformableState = rememberTransformableState { centroid, zoomChange, panChange, _ ->
        val newScale = (scale * zoomChange).coerceIn(1f, MaxZoom)
        if (newScale <= 1f) {
            scale = 1f
            offset = Offset.Zero
        } else {
            val factor = newScale / scale
            offset = (offset - centroid) * factor + centroid + panChange
            scale = newScale
            offset = clampOffset(offset, containerSize, newScale)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged { containerSize = it }
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onTap() },
                    onDoubleTap = {
                        val target = if (scale > 1f) 1f else MaxZoom
                        scope.launch {
                            animate(
                                initialValue = scale,
                                targetValue = target,
                                animationSpec = spring(),
                            ) { value, _ ->
                                scale = value
                                offset = if (value <= 1f) {
                                    Offset.Zero
                                } else {
                                    clampOffset(offset, containerSize, value)
                                }
                            }
                        }
                    },
                )
            }
            .transformable(transformableState),
    ) {
        AsyncImage(
            model = url,
            contentDescription = contentDescription,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    translationX = offset.x
                    translationY = offset.y
                },
        )
    }
}

private fun clampOffset(offset: Offset, container: IntSize, scale: Float): Offset {
    if (container.width == 0 || container.height == 0 || scale <= 1f) return Offset.Zero
    val maxX = (container.width * (scale - 1f)) / 2f
    val maxY = (container.height * (scale - 1f)) / 2f
    return Offset(
        x = offset.x.coerceIn(-maxX, maxX),
        y = offset.y.coerceIn(-maxY, maxY),
    )
}