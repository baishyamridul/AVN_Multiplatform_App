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
    val fileDir = remember {
        File(System.getProperty("java.io.tmpdir"), "pano_${UUID.randomUUID()}").also { it.mkdirs() }
    }
    var ready by remember { mutableStateOf(false) }

    LaunchedEffect(fileDir) {
        val loader = object {}.javaClass.classLoader
        loader.getResource("pannellum/pannellum.js")!!.openStream().use { it.copyTo(File(fileDir, "pannellum.js").outputStream()) }
        loader.getResource("pannellum/pannellum.css")!!.openStream().use { it.copyTo(File(fileDir, "pannellum.css").outputStream()) }
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
