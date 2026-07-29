package tech.sumato.avn.mp.component.image360

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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
    val html = remember(configUrl) { generateHtml(configUrl) }
    val tempDir = remember {
        NSTemporaryDirectory() + "pano_${NSUUID().UUIDString}/"
    }

    LaunchedEffect(tempDir) {
        val fm = NSFileManager.defaultManager
        fm.createDirectoryAtPath(tempDir, true, null, null)

        val bundle = NSBundle.mainBundle
        copyBundleFile(bundle, "pannellum/pannellum", "js", tempDir, "pannellum.js")
        copyBundleFile(bundle, "pannellum/pannellum", "css", tempDir, "pannellum.css")

        writeStringToFile(tempDir + "index.html", html)
    }

    val webViewState = rememberPlatformWebViewState(
        url = "file://$tempDir/index.html",
        javaScriptEnabled = true,
        allowsFileAccess = true,
    )

    PlatformWebView(
        state = webViewState,
        modifier = Modifier.fillMaxSize(),
    )
}

@OptIn(ExperimentalForeignApi::class)
private fun copyBundleFile(bundle: NSBundle, name: String, type: String, destDir: String, destName: String) {
    val srcPath = bundle.pathForResource(name, ofType = type) ?: error("Resource not found: $name.$type")
    val fm = NSFileManager.defaultManager
    fm.copyItemAtPath(srcPath, destDir + destName, null)
}

@OptIn(ExperimentalForeignApi::class)
private fun writeStringToFile(path: String, content: String) {
    val data = content.encodeToByteArray()
    val file = fopen(path, "wb") ?: error("Cannot open: $path")
    try {
        data.usePinned { pinned ->
            fwrite(pinned.addressOf(0), 1uL, data.size.toULong(), file)
        }
    } finally {
        fclose(file)
    }
}
