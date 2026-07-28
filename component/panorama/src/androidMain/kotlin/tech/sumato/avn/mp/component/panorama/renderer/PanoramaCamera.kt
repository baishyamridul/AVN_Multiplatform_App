package tech.sumato.avn.mp.component.panorama.renderer

import android.opengl.Matrix
import kotlin.math.tan
import kotlin.math.sqrt

class PanoramaCamera {
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val mvpMatrix = FloatArray(16)
    private val tempMatrix = FloatArray(16)

    fun setProjection(hfov: Float, aspect: Float, near: Float = 0.1f, far: Float = 100f) {
        Matrix.perspectiveM(projectionMatrix, 0, hfov, aspect, near, far)
    }

    fun setView(yaw: Float, pitch: Float) {
        Matrix.setIdentityM(viewMatrix, 0)
        Matrix.rotateM(viewMatrix, 0, pitch, 1f, 0f, 0f)
        Matrix.rotateM(viewMatrix, 0, yaw, 0f, 1f, 0f)
    }

    fun getMVPMatrix(): FloatArray {
        Matrix.multiplyMM(tempMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
        System.arraycopy(tempMatrix, 0, mvpMatrix, 0, 16)
        return mvpMatrix
    }
}
