package tech.sumato.avn.mp.component.image360

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.saralapps.composemultiplatformwebview.PlatformWebView
import com.saralapps.composemultiplatformwebview.rememberPlatformWebViewState
import java.io.File
import java.util.UUID

@Composable
actual fun PanoImageViewer(configUrl: String) {
    val context = LocalContext.current
    val html = remember(configUrl) { generateHtml(configUrl) }
    val fileDir = remember {
        File(context.cacheDir, "pano_${UUID.randomUUID()}").also { it.mkdirs() }
    }

    LaunchedEffect(fileDir) {
        File(fileDir, "index.html").writeText(html)
        copyAssetToFile(context, "pannellum/pannellum.js", File(fileDir, "pannellum.js"))
        copyAssetToFile(context, "pannellum/pannellum.css", File(fileDir, "pannellum.css"))
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

private fun copyAssetToFile(context: Context, assetPath: String, outputFile: File) {
    context.assets.open(assetPath).use { input ->
        outputFile.outputStream().use { output ->
            input.copyTo(output)
        }
    }
}
