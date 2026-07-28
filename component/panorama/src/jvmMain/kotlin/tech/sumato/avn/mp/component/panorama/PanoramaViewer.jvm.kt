package tech.sumato.avn.mp.component.panorama

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import io.ktor.client.HttpClient
import io.ktor.client.engine.java.Java
import tech.sumato.avn.mp.component.panorama.gesture.panoramaGestures
import tech.sumato.avn.mp.component.panorama.network.PanoramaRepository
import tech.sumato.avn.mp.component.panorama.tiles.CubeFace
import tech.sumato.avn.mp.component.panorama.tiles.MultiresTileLoader
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO

private val faceToGeometryIndex = mapOf(
    CubeFace.FRONT to 0,
    CubeFace.BACK to 1,
    CubeFace.LEFT to 2,
    CubeFace.RIGHT to 3,
    CubeFace.UP to 4,
    CubeFace.DOWN to 5,
)

@Composable
actual fun PanoramaViewer(
    configUrl: String,
    modifier: Modifier,
    state: PanoramaState,
    onLoadingChanged: (Boolean) -> Unit,
    onError: (Throwable) -> Unit,
) {
    val renderer = remember { SoftwareRenderer() }
    state.version

    LaunchedEffect(configUrl) {
        if (configUrl.isBlank()) return@LaunchedEffect
        try {
            state.isLoading = true
            onLoadingChanged(true)

            val httpClient = HttpClient(Java)
            val repository = PanoramaRepository(httpClient)
            val config = repository.loadConfig(configUrl)
            val resolver = repository.createResolver(configUrl, config)

            val faceImages = MutableList<ImageBitmap?>(6) { null }
            for ((face, geometryIndex) in faceToGeometryIndex) {
                val url = resolver.fallbackUrl(face)
                val bytes = repository.loadTileBytes(url)
                val bufferedImage = ImageIO.read(ByteArrayInputStream(bytes))
                if (bufferedImage != null) {
                    faceImages[geometryIndex] = bufferedImage.toComposeImageBitmap()
                }
            }
            renderer.setFaceImages(faceImages)

            val tileLoader = MultiresTileLoader(repository, resolver, config.multiRes)
            try {
                tileLoader.loadProgressive(
                    startLevel = 1,
                    onLevelUpdate = { level, tiles ->
                        for ((face, geometryIndex) in faceToGeometryIndex) {
                            val faceTiles = tiles[face] ?: continue
                            val compositeBitmap = TileCompositor.compositeFaceTiles(
                                tiles = faceTiles,
                                tileResolution = config.multiRes.tileResolution,
                            )
                            renderer.updateFaceImage(geometryIndex, compositeBitmap)
                        }
                    },
                )
            } catch (_: Exception) {
            }

            state.isLoading = false
            onLoadingChanged(false)
            httpClient.close()
        } catch (e: Exception) {
            state.error = e
            state.isLoading = false
            onError(e)
            onLoadingChanged(false)
        }
    }

    Canvas(
        modifier = modifier
            .panoramaGestures(state)
            .fillMaxSize(),
    ) {
        renderer.renderToCanvas(this, state.yaw, state.pitch, state.hfov)
    }

    DisposableEffect(Unit) {
        onDispose {
            renderer.dispose()
        }
    }
}
