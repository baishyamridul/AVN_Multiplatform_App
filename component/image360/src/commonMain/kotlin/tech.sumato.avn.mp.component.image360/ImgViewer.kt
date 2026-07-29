package tech.sumato.avn.mp.component.image360

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import avnmultiplatformapp.component.image360.generated.resources.Res
import com.saralapps.composemultiplatformwebview.PlatformWebView
import com.saralapps.composemultiplatformwebview.rememberPlatformWebViewState


@Composable
fun ImgViewer() {

    val url = Res.getUri("files/360viewer.html")

    val webViewState = rememberPlatformWebViewState(
        url = url,
        javaScriptEnabled = true,
        allowsFileAccess = true
    )

    PlatformWebView(state = webViewState, modifier = Modifier.fillMaxSize())

}