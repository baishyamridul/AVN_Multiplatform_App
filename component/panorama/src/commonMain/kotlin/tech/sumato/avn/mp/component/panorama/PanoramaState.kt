package tech.sumato.avn.mp.component.panorama

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember

@Stable
class PanoramaState(
    initialYaw: Float = 0f,
    initialPitch: Float = 0f,
    initialHfov: Float = 100f,
) {
    var yaw: Float = initialYaw
        internal set
    var pitch: Float = initialPitch
        internal set
    var hfov: Float = initialHfov
        internal set

    var isLoading: Boolean = true
        internal set
    var error: Throwable? = null
        internal set

    val minHfov: Float = 10f
    val maxHfov: Float = 120f

    private var _version: Int = 0
    val version: Int get() = _version

    fun setOrientation(yaw: Float, pitch: Float, hfov: Float) {
        this.yaw = yaw
        this.pitch = pitch
        this.hfov = hfov
        _version++
    }

    fun normalizeYaw(angle: Float): Float {
        return ((angle % 360f) + 360f) % 360f
    }

    fun clampPitch(angle: Float): Float {
        return angle.coerceIn(-89f, 89f)
    }

    fun clampHfov(fov: Float): Float {
        return fov.coerceIn(minHfov, maxHfov)
    }
}

@Composable
fun rememberPanoramaState(
    initialYaw: Float = 0f,
    initialPitch: Float = 0f,
    initialHfov: Float = 100f,
): PanoramaState {
    return remember { PanoramaState(initialYaw, initialPitch, initialHfov) }
}
