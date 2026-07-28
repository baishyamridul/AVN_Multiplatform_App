package tech.sumato.avn.mp.component.panorama.renderer

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES30
import android.opengl.GLUtils

class TextureManager {

    private val textures = mutableMapOf<Int, Int>()

    fun createFallbackTexture(color: Int): Int {
        val pixels = IntArray(4) { color }
        val bitmap = Bitmap.createBitmap(pixels, 2, 2, Bitmap.Config.ARGB_8888)
        return uploadBitmap(bitmap)
    }

    fun uploadBitmap(bitmap: Bitmap): Int {
        val textures = IntArray(1)
        GLES30.glGenTextures(1, textures, 0)
        val textureId = textures[0]

        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE)

        GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bitmap, 0)
        GLES30.glGenerateMipmap(GLES30.GL_TEXTURE_2D)

        this.textures[textureId] = textureId
        return textureId
    }

    fun loadBitmap(bytes: ByteArray): Int {
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            ?: throw RuntimeException("Failed to decode bitmap")
        val id = uploadBitmap(bitmap)
        bitmap.recycle()
        return id
    }

    fun deleteTexture(textureId: Int) {
        GLES30.glDeleteTextures(1, intArrayOf(textureId), 0)
        textures.remove(textureId)
    }

    fun cleanup() {
        if (textures.isNotEmpty()) {
            GLES30.glDeleteTextures(textures.size, textures.keys.toIntArray(), 0)
            textures.clear()
        }
    }
}
