package tech.sumato.avn.mp.component.panorama

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO

object TileCompositor {

    fun compositeFaceTiles(
        tiles: List<List<ByteArray>>,
        tileResolution: Int,
    ): ImageBitmap {
        val cols = tiles[0].size
        val rows = tiles.size
        val faceSize = tileResolution * cols

        val faceImage = BufferedImage(faceSize, faceSize, BufferedImage.TYPE_INT_RGB)
        val g = faceImage.createGraphics()

        for (y in 0 until rows) {
            for (x in 0 until cols) {
                val tileBytes = tiles[y][x]
                val tileImage = ImageIO.read(ByteArrayInputStream(tileBytes))
                if (tileImage != null) {
                    g.drawImage(
                        tileImage,
                        x * tileResolution, y * tileResolution,
                        tileResolution, tileResolution,
                        null,
                    )
                    tileImage.flush()
                }
            }
        }
        g.dispose()

        return faceImage.toComposeImageBitmap()
    }
}
