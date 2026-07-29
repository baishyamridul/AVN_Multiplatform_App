package tech.sumato.avn.mp.component.image360

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.saralapps.composemultiplatformwebview.PlatformWebView
import com.saralapps.composemultiplatformwebview.rememberPlatformWebViewState
import java.io.File
import java.util.UUID

@Composable
actual fun PanoImageViewer(configUrl: String) {
    val html = remember(configUrl) { generateHtml(configUrl) }
    val fileDir = remember {
        File(System.getProperty("java.io.tmpdir"), "pano_${UUID.randomUUID()}").also { it.mkdirs() }
    }

    LaunchedEffect(fileDir) {
        File(fileDir, "index.html").writeText(html)
        File(fileDir, "pannellum.js").writeBytes(readResourceBytes("pannellum/pannellum.js"))
        File(fileDir, "pannellum.css").writeBytes(readResourceBytes("pannellum/pannellum.css"))
    }

    val webViewState = rememberPlatformWebViewState(
        url = "file://${File(fileDir, "index.html").absolutePath}",
        javaScriptEnabled = true,
        allowsFileAccess = true,
    )

    PlatformWebView(
        state = webViewState,
        modifier = Modifier.fillMaxSize(),
    )
}

private fun readResourceBytes(path: String): ByteArray {
    val url = object {}.javaClass.classLoader.getResource(path)
        ?: error("Resource not found: $path")
    return url.readBytes()
}
