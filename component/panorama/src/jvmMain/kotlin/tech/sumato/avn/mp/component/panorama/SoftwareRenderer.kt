package tech.sumato.avn.mp.component.panorama

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize

class SoftwareRenderer {

    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val mvpMatrix = FloatArray(16)

    private val cubeVertices = arrayOf(
        floatArrayOf(-1f, -1f, 1f),  floatArrayOf(1f, -1f, 1f),
        floatArrayOf(1f, 1f, 1f),  floatArrayOf(-1f, 1f, 1f),
        floatArrayOf(1f, -1f, -1f), floatArrayOf(-1f, -1f, -1f),
        floatArrayOf(-1f, 1f, -1f), floatArrayOf(1f, 1f, -1f),
        floatArrayOf(-1f, -1f, -1f), floatArrayOf(-1f, -1f, 1f),
        floatArrayOf(-1f, 1f, 1f),  floatArrayOf(-1f, 1f, -1f),
        floatArrayOf(1f, -1f, 1f),  floatArrayOf(1f, -1f, -1f),
        floatArrayOf(1f, 1f, -1f),  floatArrayOf(1f, 1f, 1f),
        floatArrayOf(-1f, 1f, 1f),  floatArrayOf(1f, 1f, 1f),
        floatArrayOf(1f, 1f, -1f),  floatArrayOf(-1f, 1f, -1f),
        floatArrayOf(-1f, -1f, -1f), floatArrayOf(1f, -1f, -1f),
        floatArrayOf(1f, -1f, 1f),  floatArrayOf(-1f, -1f, 1f),
    )

    private val faceColors = listOf(
        androidx.compose.ui.graphics.Color(0xFFFF0000),
        androidx.compose.ui.graphics.Color(0xFF00FF00),
        androidx.compose.ui.graphics.Color(0xFF0000FF),
        androidx.compose.ui.graphics.Color(0xFFFFFF00),
        androidx.compose.ui.graphics.Color(0xFFFF00FF),
        androidx.compose.ui.graphics.Color(0xFF00FFFF),
    )

    private val faceIndices = listOf(
        intArrayOf(0, 1, 2, 3),
        intArrayOf(4, 5, 6, 7),
        intArrayOf(8, 9, 10, 11),
        intArrayOf(12, 13, 14, 15),
        intArrayOf(16, 17, 18, 19),
        intArrayOf(20, 21, 22, 23),
    )

    private var faceImages: MutableList<ImageBitmap?> = MutableList(6) { null }

    fun setFaceImages(images: List<ImageBitmap?>) {
        for (i in images.indices) {
            if (i < 6) faceImages[i] = images[i]
        }
    }

    fun updateFaceImage(faceIndex: Int, image: ImageBitmap) {
        if (faceIndex in 0 until 6) {
            faceImages[faceIndex] = image
        }
    }

    private val ndcToScreen = FloatArray(16)
    private val combined = FloatArray(16)

    fun renderToCanvas(
        drawScope: DrawScope,
        yaw: Float,
        pitch: Float,
        hfov: Float,
    ) {
        val w = drawScope.size.width
        val h = drawScope.size.height
        if (w <= 0 || h <= 0) return

        val aspect = w / h
        setProjection(hfov, aspect)
        setView(yaw, pitch)

        ndcToScreen[0] = w / 2f
        ndcToScreen[1] = 0f
        ndcToScreen[2] = 0f
        ndcToScreen[3] = w / 2f
        ndcToScreen[4] = 0f
        ndcToScreen[5] = -h / 2f
        ndcToScreen[6] = 0f
        ndcToScreen[7] = h / 2f
        ndcToScreen[8] = 0f
        ndcToScreen[9] = 0f
        ndcToScreen[10] = 1f
        ndcToScreen[11] = 0f
        ndcToScreen[12] = 0f
        ndcToScreen[13] = 0f
        ndcToScreen[14] = 0f
        ndcToScreen[15] = 1f

        multiplyInto(combined, ndcToScreen, mvpMatrix)

        for (faceIdx in faceIndices.indices) {
            val idx = faceIndices[faceIdx]
            val projected = idx.map { i ->
                val vertex = cubeVertices[i]
                val clip = transformPoint(combined, vertex[0], vertex[1], vertex[2])
                if (clip[3] != 0f) {
                    Offset(clip[0] / clip[3], clip[1] / clip[3])
                } else {
                    Offset.Zero
                }
            }

            val image = faceImages[faceIdx]
            if (image != null) {
                val minX = projected.minOf { it.x }
                val minY = projected.minOf { it.y }
                val maxX = projected.maxOf { it.x }
                val maxY = projected.maxOf { it.y }
                val bw = maxX - minX
                val bh = maxY - minY
                if (bw > 0f && bh > 0f) {
                    val canvas = drawScope.drawContext.canvas
                    canvas.save()
                    canvas.clipPath(
                        path = Path().apply {
                            moveTo(projected[0].x, projected[0].y)
                            lineTo(projected[1].x, projected[1].y)
                            lineTo(projected[2].x, projected[2].y)
                            lineTo(projected[3].x, projected[3].y)
                            close()
                        },
                        clipOp = ClipOp.Intersect,
                    )
                    drawScope.drawImage(
                        image = image,
                        dstOffset = IntOffset(minX.toInt(), minY.toInt()),
                        dstSize = IntSize(bw.toInt(), bh.toInt()),
                    )
                    canvas.restore()
                }
            } else {
                val path = Path().apply {
                    moveTo(projected[0].x, projected[0].y)
                    lineTo(projected[1].x, projected[1].y)
                    lineTo(projected[2].x, projected[2].y)
                    lineTo(projected[3].x, projected[3].y)
                    close()
                }
                drawScope.drawPath(path, faceColors[faceIdx], style = Fill)
            }
        }
    }

    private fun setProjection(hfov: Float, aspect: Float) {
        val fovRad = Math.toRadians((hfov / 2f).toDouble())
        val f = 1f / kotlin.math.tan(fovRad).toFloat()
        val near = 0.1f
        val far = 100f

        projectionMatrix[0] = f / aspect
        projectionMatrix[1] = 0f
        projectionMatrix[2] = 0f
        projectionMatrix[3] = 0f
        projectionMatrix[4] = 0f
        projectionMatrix[5] = f
        projectionMatrix[6] = 0f
        projectionMatrix[7] = 0f
        projectionMatrix[8] = 0f
        projectionMatrix[9] = 0f
        projectionMatrix[10] = -(far + near) / (far - near)
        projectionMatrix[11] = -1f
        projectionMatrix[12] = 0f
        projectionMatrix[13] = 0f
        projectionMatrix[14] = -(2f * far * near) / (far - near)
        projectionMatrix[15] = 0f
    }

    private fun setView(yaw: Float, pitch: Float) {
        val yawRad = Math.toRadians(yaw.toDouble())
        val pitchRad = Math.toRadians(pitch.toDouble())
        val cy = kotlin.math.cos(yawRad).toFloat()
        val sy = kotlin.math.sin(yawRad).toFloat()
        val cp = kotlin.math.cos(pitchRad).toFloat()
        val sp = kotlin.math.sin(pitchRad).toFloat()

        viewMatrix[0] = cy
        viewMatrix[1] = 0f
        viewMatrix[2] = -sy
        viewMatrix[3] = 0f
        viewMatrix[4] = sy * sp
        viewMatrix[5] = cp
        viewMatrix[6] = cy * sp
        viewMatrix[7] = 0f
        viewMatrix[8] = sy * cp
        viewMatrix[9] = -sp
        viewMatrix[10] = cy * cp
        viewMatrix[11] = 0f
        viewMatrix[12] = 0f
        viewMatrix[13] = 0f
        viewMatrix[14] = 0f
        viewMatrix[15] = 1f

        mvpMatrix.forEachIndexed { i, _ -> mvpMatrix[i] = 0f }
        multiplyInto(mvpMatrix, projectionMatrix, viewMatrix)
    }

    private fun transformPoint(mat: FloatArray, x: Float, y: Float, z: Float): FloatArray {
        return floatArrayOf(
            mat[0] * x + mat[4] * y + mat[8] * z + mat[12],
            mat[1] * x + mat[5] * y + mat[9] * z + mat[13],
            mat[2] * x + mat[6] * y + mat[10] * z + mat[14],
            mat[3] * x + mat[7] * y + mat[11] * z + mat[15],
        )
    }

    private fun multiplyInto(result: FloatArray, a: FloatArray, b: FloatArray) {
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                var sum = 0f
                for (k in 0 until 4) {
                    sum += a[i * 4 + k] * b[k * 4 + j]
                }
                result[i * 4 + j] = sum
            }
        }
    }

    fun dispose() {
    }
}
