package tech.sumato.avn.mp.component.image_viewer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil3.Image
import coil3.ImageLoader
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.github.panpf.zoomimage.CoilZoomAsyncImage


@Composable
fun ImageViewer(
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

        val context =
            LocalPlatformContext.current

        Box(modifier = Modifier.fillMaxSize()) {
            when (source) {
                is ImageSource.Photo -> {
                    CoilZoomAsyncImage(
                        model = ImageRequest.Builder(context).apply {
                            data(source.url)
                            crossfade(true)
                        }.build(),
                        contentDescription = "view image",
                        imageLoader = ImageLoader(context),
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                is ImageSource.Pano -> {
                    //
                }
            }

            IconButton(
                onClick = {
                    onDismissRequest()
                },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(Icons.Default.Close, "")
            }

        }

    }


}