package tech.sumato.avn.mp.component.panorama

import android.graphics.BitmapFactory
import android.opengl.GLSurfaceView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import tech.sumato.avn.mp.component.panorama.gesture.panoramaGestures
import tech.sumato.avn.mp.component.panorama.network.PanoramaRepository
import tech.sumato.avn.mp.component.panorama.renderer.PanoramaRenderer
import tech.sumato.avn.mp.component.panorama.renderer.TileCompositor
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

@Composable
actual fun PanoramaViewer(
    configUrl: String,
    modifier: Modifier,
    state: PanoramaState,
    onLoadingChanged: (Boolean) -> Unit,
    onError: (Throwable) -> Unit,
) {
    val renderer = remember { PanoramaRenderer(state) }
    val glSurfaceViewRef = remember { mutableStateOf<GLSurfaceView?>(null) }
    val compositor = remember { TileCompositor() }

    LaunchedEffect(configUrl) {
        if (configUrl.isBlank()) return@LaunchedEffect
        try {
            state.isLoading = true
            onLoadingChanged(true)

            val httpClient = HttpClient(OkHttp)
            val repository = PanoramaRepository(httpClient)
            val config = repository.loadConfig(configUrl)
            val resolver = repository.createResolver(configUrl, config)
            val textureManager = renderer.getTextureManager()

            for ((face, geometryIndex) in faceToGeometryIndex) {
                val url = resolver.fallbackUrl(face)
                val bytes = repository.loadTileBytes(url)
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                if (bitmap != null) {
                    glSurfaceViewRef.value?.queueEvent {
                        val textureId = textureManager.uploadBitmap(bitmap)
                        bitmap.recycle()
                        renderer.updateFaceTexture(geometryIndex, textureId)
                        glSurfaceViewRef.value?.requestRender()
                    }
                }
            }

            val tileLoader = MultiresTileLoader(repository, resolver, config.multiRes)
            try {
                tileLoader.loadProgressive(
                    startLevel = 1,
                    onLevelUpdate = { level, tiles ->
                        for ((face, geometryIndex) in faceToGeometryIndex) {
                            val faceTiles = tiles[face] ?: continue
                            val faceBitmap = compositor.compositeFaceTiles(
                                tiles = faceTiles,
                                tileResolution = config.multiRes.tileResolution,
                            )
                            glSurfaceViewRef.value?.queueEvent {
                                val textureId = textureManager.uploadBitmap(faceBitmap)
                                if (!faceBitmap.isRecycled) faceBitmap.recycle()
                                renderer.updateFaceTexture(geometryIndex, textureId)
                                glSurfaceViewRef.value?.requestRender()
                            }
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

    AndroidView(
        modifier = modifier.panoramaGestures(state),
        factory = { context ->
            GLSurfaceView(context).apply {
                setEGLContextClientVersion(3)
                setRenderer(renderer)
                renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
                glSurfaceViewRef.value = this
            }
        },
        update = { glSurfaceView ->
            glSurfaceView.requestRender()
        },
    )

    DisposableEffect(Unit) {
        onDispose { }
    }
}
