package tech.sumato.avn.mp.component.panorama

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.CoreGraphics.CGSizeMake
import platform.Foundation.NSTemporaryDirectory
import platform.UIKit.UIGraphicsBeginImageContext
import platform.UIKit.UIGraphicsEndImageContext
import platform.UIKit.UIGraphicsGetImageFromCurrentImageContext
import platform.UIKit.UIImage
import platform.posix.fclose
import platform.posix.fopen
import platform.posix.fwrite
import platform.posix.remove

@OptIn(ExperimentalForeignApi::class)
object TileCompositor {

    private var tempFileCounter = 0

    private fun writeTempImageFile(bytes: ByteArray): String {
        val dir = NSTemporaryDirectory().trimEnd('/')
        val fileName = "pano_tile_${tempFileCounter++}.jpg"
        val filePath = "$dir/$fileName"
        val file = fopen(filePath, "wb")
        if (file != null) {
            bytes.usePinned { pinned ->
                fwrite(pinned.addressOf(0), 1.toULong(), bytes.size.toULong(), file)
            }
            fclose(file)
        }
        return filePath
    }

    fun compositeFaceTiles(
        tiles: List<List<ByteArray>>,
        tileResolution: Int,
        onImageReady: (UIImage) -> Unit,
    ) {
        val cols = tiles[0].size
        val rows = tiles.size
        val faceSize = tileResolution * cols

        val tileFilePaths = mutableListOf<String>()
        for (y in 0 until rows) {
            for (x in 0 until cols) {
                tileFilePaths.add(writeTempImageFile(tiles[y][x]))
            }
        }

        UIGraphicsBeginImageContext(CGSizeMake(faceSize.toDouble(), faceSize.toDouble()))

        for (y in 0 until rows) {
            for (x in 0 until cols) {
                val tilePath = tileFilePaths[y * cols + x]
                val tileImage = UIImage.imageWithContentsOfFile(tilePath)
                tileImage?.drawInRect(
                    platform.CoreGraphics.CGRectMake(
                        (x * tileResolution).toDouble(),
                        (y * tileResolution).toDouble(),
                        tileResolution.toDouble(),
                        tileResolution.toDouble(),
                    )
                )
            }
        }

        val compositeImage = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()

        for (path in tileFilePaths) {
            remove(path)
        }

        if (compositeImage != null) {
            onImageReady(compositeImage)
        }
    }
}
