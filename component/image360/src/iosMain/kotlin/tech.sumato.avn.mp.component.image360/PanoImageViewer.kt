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
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSBundle
import platform.Foundation.NSFileManager
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSUUID
import platform.posix.fclose
import platform.posix.fopen
import platform.posix.fwrite

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun PanoImageViewer(configUrl: String) {
    val tempDir = remember {
        NSTemporaryDirectory() + "pano_${NSUUID().UUIDString}/"
    }
    var ready by remember { mutableStateOf(false) }

    LaunchedEffect(tempDir) {
        NSFileManager.defaultManager.createDirectoryAtPath(tempDir, true, null, null)

        val bundle = NSBundle.mainBundle
        val jsPath = bundle.pathForResource("pannellum/pannellum", ofType = "js") ?: error("pannellum.js not found")
        val cssPath = bundle.pathForResource("pannellum/pannellum", ofType = "css") ?: error("pannellum.css not found")
        NSFileManager.defaultManager.copyItemAtPath(jsPath, tempDir + "pannellum.js", null)
        NSFileManager.defaultManager.copyItemAtPath(cssPath, tempDir + "pannellum.css", null)

        writeUtf8(tempDir + "360viewer.html", viewerHtml(configUrl))
        ready = true
    }

    if (ready) {
        val webViewState = rememberPlatformWebViewState(
            url = "file://$tempDir/360viewer.html",
            javaScriptEnabled = true,
            allowsFileAccess = true,
        )
        PlatformWebView(
            state = webViewState,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun writeUtf8(path: String, content: String) {
    val bytes = content.encodeToByteArray()
    val file = fopen(path, "wb") ?: error("Cannot open: $path")
    try {
        bytes.usePinned { pinned ->
            fwrite(pinned.addressOf(0), 1uL, bytes.size.toULong(), file)
        }
    } finally {
        fclose(file)
    }
}
