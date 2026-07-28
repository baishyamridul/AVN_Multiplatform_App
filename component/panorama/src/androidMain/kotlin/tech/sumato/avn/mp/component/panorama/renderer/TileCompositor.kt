package tech.sumato.avn.mp.component.panorama.renderer

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect

class TileCompositor {

    fun compositeFaceTiles(
        tiles: List<List<ByteArray>>,
        tileResolution: Int,
    ): Bitmap {
        val rows = tiles.size
        val cols = tiles[0].size
        val faceSize = tileResolution * cols

        val faceBitmap = Bitmap.createBitmap(faceSize, faceSize, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(faceBitmap)

        for (y in 0 until rows) {
            for (x in 0 until cols) {
                val tileBytes = tiles[y][x]
                val tileBitmap = BitmapFactory.decodeByteArray(tileBytes, 0, tileBytes.size)
                if (tileBitmap != null) {
                    val srcRect = Rect(0, 0, tileBitmap.width, tileBitmap.height)
                    val dstRect = Rect(
                        x * tileResolution, y * tileResolution,
                        (x + 1) * tileResolution, (y + 1) * tileResolution,
                    )
                    canvas.drawBitmap(tileBitmap, srcRect, dstRect, null)
                    tileBitmap.recycle()
                }
            }
        }

        return faceBitmap
    }
}
