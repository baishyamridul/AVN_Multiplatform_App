package tech.sumato.avn.mp.component.panorama.gesture

import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import tech.sumato.avn.mp.component.panorama.PanoramaState

private const val DRAG_SENSITIVITY = 0.3f

fun Modifier.panoramaGestures(state: PanoramaState): Modifier = this
    .pointerInput(Unit) {
        detectTransformGestures { _, pan, zoom, _ ->
            val yawDelta = -pan.x * DRAG_SENSITIVITY
            val pitchDelta = -pan.y * DRAG_SENSITIVITY
            val hfovScale = 1f / zoom

            val newYaw = state.normalizeYaw(state.yaw + yawDelta)
            val newPitch = state.clampPitch(state.pitch + pitchDelta)
            val newHfov = state.clampHfov(state.hfov * hfovScale)
            state.setOrientation(newYaw, newPitch, newHfov)
        }
    }
