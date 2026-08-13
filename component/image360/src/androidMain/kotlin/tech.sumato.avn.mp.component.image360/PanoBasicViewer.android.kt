package tech.sumato.avn.mp.component.image360

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.saralapps.composemultiplatformwebview.PlatformWebView
import com.saralapps.composemultiplatformwebview.rememberPlatformWebViewState
import java.io.File
import java.util.UUID

@Composable
actual fun PanoBasicViewer(imageUrl: String) {

    val context = LocalContext.current
    val fileDir = remember {
        File(context.cacheDir, "pano_${UUID.randomUUID()}").also { it.mkdirs() }
    }
    var ready by remember { mutableStateOf(false) }

    LaunchedEffect(fileDir) {

        context.assets.open("pannellum/pannellum.js").use {
            it.copyTo(File(fileDir, "pannellum.js").outputStream())
        }

        context.assets.open("pannellum/pannellum.css").use {
            it.copyTo(File(fileDir, "pannellum.css").outputStream())
        }

//        val loader = context.javaClass.classLoader!!
//        loader.getResourceAsStream("pannellum/pannellum.js")!!
//            .use { it.copyTo(File(fileDir, "pannellum.js").outputStream()) }
//        loader.getResourceAsStream("pannellum/pannellum.css")!!
//            .use { it.copyTo(File(fileDir, "pannellum.css").outputStream()) }

        File(fileDir, "360basicviewer.html").writeText(basicViewerHtml(imageUrl))
        ready = true
    }

    if (ready) {
        val webViewState = rememberPlatformWebViewState(
            url = "file://${File(fileDir, "360basicviewer.html").absolutePath}",
            javaScriptEnabled = true,
            allowsFileAccess = true,
        )
        PlatformWebView(
            state = webViewState,
            modifier = Modifier.fillMaxSize(),
        )
    }

}