package tech.sumato.avn.mp.component.panorama

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGRectMake
import tech.sumato.avn.mp.component.panorama.gesture.panoramaGestures
import tech.sumato.avn.mp.component.panorama.network.PanoramaRepository
import tech.sumato.avn.mp.component.panorama.tiles.CubeFace
import tech.sumato.avn.mp.component.panorama.tiles.MultiresTileLoader

private val faceToGeometryIndex = mapOf(
    CubeFace.FRONT to 0,
    CubeFace.BACK to 1,
    CubeFace.LEFT to 2,
    CubeFace.RIGHT to 3,
    CubeFace.UP to 4,
    CubeFace.DOWN to 5,
)

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun PanoramaViewer(
    configUrl: String,
    modifier: Modifier,
    state: PanoramaState,
    onLoadingChanged: (Boolean) -> Unit,
    onError: (Throwable) -> Unit,
) {
    val setup = remember { SceneKitSetup() }
    state.version

    LaunchedEffect(configUrl) {
        if (configUrl.isBlank()) return@LaunchedEffect
        try {
            state.isLoading = true
            onLoadingChanged(true)

            val httpClient = HttpClient(Darwin)
            val repository = PanoramaRepository(httpClient)
            val config = repository.loadConfig(configUrl)
            val resolver = repository.createResolver(configUrl, config)

            for ((face, geometryIndex) in faceToGeometryIndex) {
                val url = resolver.fallbackUrl(face)
                val bytes = repository.loadTileBytes(url)
                setup.updateFaceTexture(geometryIndex, bytes)
            }

            val tileLoader = MultiresTileLoader(repository, resolver, config.multiRes)
            try {
                tileLoader.loadProgressive(
                    startLevel = 1,
                    onLevelUpdate = { level, tiles ->
                        for ((face, geometryIndex) in faceToGeometryIndex) {
                            val faceTiles = tiles[face] ?: continue
                            TileCompositor.compositeFaceTiles(
                                tiles = faceTiles,
                                tileResolution = config.multiRes.tileResolution,
                                onImageReady = { image ->
                                    setup.updateFaceMaterial(geometryIndex, image)
                                },
                            )
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

    UIKitView(
        modifier = modifier.panoramaGestures(state),
        factory = {
            setup.createSceneView(CGRectMake(0.0, 0.0, 0.0, 0.0))
        },
        update = { _ ->
            setup.updateCamera(state.yaw, state.pitch, state.hfov)
        },
    )

    DisposableEffect(Unit) {
        onDispose { }
    }
}
