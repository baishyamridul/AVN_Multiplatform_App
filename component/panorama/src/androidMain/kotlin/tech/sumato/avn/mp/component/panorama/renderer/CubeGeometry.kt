package tech.sumato.avn.mp.component.panorama.renderer

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

class CubeGeometry {

    private val _vertexBuffer: FloatBuffer
    private val _indexBuffer: ShortBuffer

    val vertexCount: Int
    val indexCount: Int

    companion object {
        const val POSITION_COMPONENTS = 3
        const val TEX_COORD_COMPONENTS = 2
        const val STRIDE = (POSITION_COMPONENTS + TEX_COORD_COMPONENTS) * 4

        private val CUBE_VERTICES = floatArrayOf(
            // Front face (+Z)
            -1f, -1f,  1f,  0f, 0f,
             1f, -1f,  1f,  1f, 0f,
             1f,  1f,  1f,  1f, 1f,
            -1f,  1f,  1f,  0f, 1f,
            // Back face (-Z)
             1f, -1f, -1f,  0f, 0f,
            -1f, -1f, -1f,  1f, 0f,
            -1f,  1f, -1f,  1f, 1f,
             1f,  1f, -1f,  0f, 1f,
            // Left face (-X)
            -1f, -1f, -1f,  0f, 0f,
            -1f, -1f,  1f,  1f, 0f,
            -1f,  1f,  1f,  1f, 1f,
            -1f,  1f, -1f,  0f, 1f,
            // Right face (+X)
             1f, -1f,  1f,  0f, 0f,
             1f, -1f, -1f,  1f, 0f,
             1f,  1f, -1f,  1f, 1f,
             1f,  1f,  1f,  0f, 1f,
            // Top face (+Y)
            -1f,  1f,  1f,  0f, 0f,
             1f,  1f,  1f,  1f, 0f,
             1f,  1f, -1f,  1f, 1f,
            -1f,  1f, -1f,  0f, 1f,
            // Bottom face (-Y)
            -1f, -1f, -1f,  0f, 0f,
             1f, -1f, -1f,  1f, 0f,
             1f, -1f,  1f,  1f, 1f,
            -1f, -1f,  1f,  0f, 1f,
        )

        private val CUBE_INDICES = shortArrayOf(
            0, 1, 2,  0, 2, 3,
            4, 5, 6,  4, 6, 7,
            8, 9, 10,  8, 10, 11,
            12, 13, 14,  12, 14, 15,
            16, 17, 18,  16, 18, 19,
            20, 21, 22,  20, 22, 23,
        )
    }

    init {
        _vertexBuffer = ByteBuffer
            .allocateDirect(CUBE_VERTICES.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(CUBE_VERTICES)
        _vertexBuffer.position(0)

        _indexBuffer = ByteBuffer
            .allocateDirect(CUBE_INDICES.size * 2)
            .order(ByteOrder.nativeOrder())
            .asShortBuffer()
            .put(CUBE_INDICES)
        _indexBuffer.position(0)

        vertexCount = CUBE_VERTICES.size / (POSITION_COMPONENTS + TEX_COORD_COMPONENTS)
        indexCount = CUBE_INDICES.size
    }

    fun vertexBuffer(): FloatBuffer = _vertexBuffer
    fun indexBuffer(): ShortBuffer = _indexBuffer
}
