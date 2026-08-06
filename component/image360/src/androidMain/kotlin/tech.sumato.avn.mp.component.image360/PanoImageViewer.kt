package tech.sumato.avn.mp.component.image360

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.saralapps.composemultiplatformwebview.PlatformWebView
import com.saralapps.composemultiplatformwebview.rememberPlatformWebViewState
import java.io.File
import java.util.UUID

@Composable
actual fun PanoImageViewer(configUrl: String) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val fileDir = remember {
        File(context.cacheDir, "pano_${UUID.randomUUID()}").also { it.mkdirs() }
    }
    var ready by remember { mutableStateOf(false) }

    LaunchedEffect(fileDir) {
        val loader = context.javaClass.classLoader!!
        loader.getResourceAsStream("pannellum/pannellum.js")!!.use { it.copyTo(File(fileDir, "pannellum.js").outputStream()) }
        loader.getResourceAsStream("pannellum/pannellum.css")!!.use { it.copyTo(File(fileDir, "pannellum.css").outputStream()) }
        File(fileDir, "360viewer.html").writeText(viewerHtml(configUrl))
        ready = true
    }

    if (ready) {
        val webViewState = rememberPlatformWebViewState(
            url = "file://${File(fileDir, "360viewer.html").absolutePath}",
            javaScriptEnabled = true,
            allowsFileAccess = true,
        )
        PlatformWebView(
            state = webViewState,
            modifier = Modifier.fillMaxSize(),
        )
    }
}
