package tech.sumato.avn.mp.component.panorama.renderer

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import tech.sumato.avn.mp.component.panorama.PanoramaState
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class PanoramaRenderer(private val state: PanoramaState) : GLSurfaceView.Renderer {

    private val camera = PanoramaCamera()
    private val geometry = CubeGeometry()
    private lateinit var shader: ShaderProgram
    private val textureManager = TextureManager()

    private var aPositionLocation = 0
    private var aTexCoordLocation = 0
    private var uMVPMatrixLocation = 0
    private var uTextureLocation = 0

    private var viewportWidth = 0
    private var viewportHeight = 0

    private val faceTextureIds = IntArray(6) { 0 }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES30.glClearColor(0f, 0f, 0f, 1f)
        GLES30.glDisable(GLES30.GL_CULL_FACE)
        GLES30.glEnable(GLES30.GL_DEPTH_TEST)

        shader = ShaderProgram(SHADER_VERTEX, SHADER_FRAGMENT)

        aPositionLocation = shader.getAttribLocation("aPosition")
        aTexCoordLocation = shader.getAttribLocation("aTexCoord")
        uMVPMatrixLocation = shader.getUniformLocation("uMVPMatrix")
        uTextureLocation = shader.getUniformLocation("uTexture")

        val fallbackColors = intArrayOf(
            0xFFFF0000.toInt(), 0xFF00FF00.toInt(), 0xFF0000FF.toInt(),
            0xFFFFFF00.toInt(), 0xFFFF00FF.toInt(), 0xFF00FFFF.toInt(),
        )
        for (i in 0 until 6) {
            faceTextureIds[i] = textureManager.createFallbackTexture(fallbackColors[i])
        }
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        viewportWidth = width
        viewportHeight = height
        GLES30.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)

        val aspect = viewportWidth.toFloat() / viewportHeight.toFloat()
        camera.setProjection(state.hfov, aspect)
        camera.setView(state.yaw, state.pitch)

        shader.use()
        shader.setUniformMatrix4(uMVPMatrixLocation, camera.getMVPMatrix())

        val vb = geometry.vertexBuffer()
        val ib = geometry.indexBuffer()

        vb.position(0)
        GLES30.glVertexAttribPointer(aPositionLocation, 3, GLES30.GL_FLOAT, false, 20, vb)
        GLES30.glEnableVertexAttribArray(aPositionLocation)

        vb.position(3)
        GLES30.glVertexAttribPointer(aTexCoordLocation, 2, GLES30.GL_FLOAT, false, 20, vb)
        GLES30.glEnableVertexAttribArray(aTexCoordLocation)

        for (face in 0 until 6) {
            GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, faceTextureIds[face])
            GLES30.glUniform1i(uTextureLocation, 0)

            val startIndex = face * 6
            ib.position(startIndex)
            GLES30.glDrawElements(GLES30.GL_TRIANGLES, 6, GLES30.GL_UNSIGNED_SHORT, ib)
        }

        GLES30.glDisableVertexAttribArray(aPositionLocation)
        GLES30.glDisableVertexAttribArray(aTexCoordLocation)
    }

    fun updateFaceTexture(faceIndex: Int, textureId: Int) {
        if (faceIndex in 0 until 6) {
            if (faceTextureIds[faceIndex] != 0) {
                textureManager.deleteTexture(faceTextureIds[faceIndex])
            }
            faceTextureIds[faceIndex] = textureId
        }
    }

    fun getTextureManager(): TextureManager = textureManager

    companion object {
        private val SHADER_VERTEX = """
            #version 300 es
            in vec3 aPosition;
            in vec2 aTexCoord;
            uniform mat4 uMVPMatrix;
            out vec2 vTexCoord;
            void main() {
                gl_Position = uMVPMatrix * vec4(aPosition, 1.0);
                vTexCoord = aTexCoord;
            }
        """.trimIndent()

        private val SHADER_FRAGMENT = """
            #version 300 es
            precision mediump float;
            in vec2 vTexCoord;
            uniform sampler2D uTexture;
            out vec4 fragColor;
            void main() {
                fragColor = texture(uTexture, vTexCoord);
            }
        """.trimIndent()
    }
}
