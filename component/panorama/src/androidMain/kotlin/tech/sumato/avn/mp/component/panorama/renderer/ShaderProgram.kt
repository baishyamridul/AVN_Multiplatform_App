package tech.sumato.avn.mp.component.panorama.renderer

import android.opengl.GLES30
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class ShaderProgram(vertexSource: String, fragmentSource: String) {

    private val programId: Int

    private val vertexShaderId: Int
    private val fragmentShaderId: Int

    init {
        vertexShaderId = compileShader(GLES30.GL_VERTEX_SHADER, vertexSource)
        fragmentShaderId = compileShader(GLES30.GL_FRAGMENT_SHADER, fragmentSource)
        programId = linkProgram(vertexShaderId, fragmentShaderId)
    }

    fun use() {
        GLES30.glUseProgram(programId)
    }

    fun getAttribLocation(name: String): Int {
        return GLES30.glGetAttribLocation(programId, name)
    }

    fun getUniformLocation(name: String): Int {
        return GLES30.glGetUniformLocation(programId, name)
    }

    fun setUniformMatrix4(location: Int, matrix: FloatArray) {
        GLES30.glUniformMatrix4fv(location, 1, false, matrix, 0)
    }

    fun setUniform4f(location: Int, r: Float, g: Float, b: Float, a: Float) {
        GLES30.glUniform4f(location, r, g, b, a)
    }

    fun setUniform1i(location: Int, value: Int) {
        GLES30.glUniform1i(location, value)
    }

    fun delete() {
        GLES30.glDeleteProgram(programId)
        GLES30.glDeleteShader(vertexShaderId)
        GLES30.glDeleteShader(fragmentShaderId)
    }

    companion object {
        private fun compileShader(type: Int, source: String): Int {
            val shader = GLES30.glCreateShader(type)
            GLES30.glShaderSource(shader, source)
            GLES30.glCompileShader(shader)

            val status = IntArray(1)
            GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, status, 0)
            if (status[0] == 0) {
                val log = GLES30.glGetShaderInfoLog(shader)
                GLES30.glDeleteShader(shader)
                throw RuntimeException("Shader compile error: $log")
            }
            return shader
        }

        private fun linkProgram(vertexShader: Int, fragmentShader: Int): Int {
            val program = GLES30.glCreateProgram()
            GLES30.glAttachShader(program, vertexShader)
            GLES30.glAttachShader(program, fragmentShader)
            GLES30.glLinkProgram(program)

            val status = IntArray(1)
            GLES30.glGetProgramiv(program, GLES30.GL_LINK_STATUS, status, 0)
            if (status[0] == 0) {
                val log = GLES30.glGetProgramInfoLog(program)
                GLES30.glDeleteProgram(program)
                throw RuntimeException("Program link error: $log")
            }
            return program
        }
    }

    val VERTEX_SHADER_SRC = """
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

    val FRAGMENT_SHADER_SRC = """
        #version 300 es
        precision mediump float;
        in vec2 vTexCoord;
        uniform vec4 uFaceColor;
        out vec4 fragColor;
        void main() {
            fragColor = uFaceColor;
        }
    """.trimIndent()
}
