package tech.sumato.avn.mp.component.panorama.math

import tech.sumato.avn.mp.component.panorama.tiles.CubeFace
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

fun yawPitchToDirection(yaw: Float, pitch: Float): FloatArray {
    val yawRad = yaw * (PI.toFloat() / 180f)
    val pitchRad = pitch * (PI.toFloat() / 180f)
    val cosPitch = cos(pitchRad)
    return floatArrayOf(
        sin(yawRad) * cosPitch,
        sin(pitchRad),
        -cos(yawRad) * cosPitch,
    )
}

fun directionToCubeFaceAndUV(dir: FloatArray): Triple<CubeFace, Float, Float> {
    val ax = abs(dir[0])
    val ay = abs(dir[1])
    val az = abs(dir[2])

    val (face, u, v) = when {
        ax >= ay && ax >= az && dir[0] > 0 -> Triple(CubeFace.RIGHT, -dir[2] / ax, -dir[1] / ax)
        ax >= ay && ax >= az -> Triple(CubeFace.LEFT, dir[2] / ax, -dir[1] / ax)
        ay >= ax && ay >= az && dir[1] > 0 -> Triple(CubeFace.UP, dir[0] / ay, dir[2] / ay)
        ay >= ax && ay >= az -> Triple(CubeFace.DOWN, dir[0] / ay, -dir[2] / ay)
        az >= ax && az >= ay && dir[2] > 0 -> Triple(CubeFace.FRONT, dir[0] / az, -dir[1] / az)
        else -> Triple(CubeFace.BACK, -dir[0] / az, -dir[1] / az)
    }

    val uNorm = (u + 1f) * 0.5f
    val vNorm = (v + 1f) * 0.5f
    return Triple(face, uNorm.coerceIn(0f, 1f), vNorm.coerceIn(0f, 1f))
}

fun computeFrustumPlanes(yaw: Float, pitch: Float, hfov: Float, aspect: Float): Array<FloatArray> {
    val yawRad = yaw * (PI.toFloat() / 180f)
    val pitchRad = pitch * (PI.toFloat() / 180f)
    val hfovRad = (hfov / 2f) * (PI.toFloat() / 180f)
    val vfovRad = atan2(tan(hfovRad), aspect)

    val forward = yawPitchToDirection(yaw, pitch)

    val right = floatArrayOf(cos(yawRad), 0f, sin(yawRad))
    val up = floatArrayOf(
        -sin(pitchRad) * sin(yawRad),
        cos(pitchRad),
        sin(pitchRad) * cos(yawRad),
    )

    val leftNormal = rotateAroundUp(forward, right, hfovRad)
    val rightNormal = rotateAroundUp(forward, right, -hfovRad)
    val topNormal = rotateAroundAxis(forward, up, right, vfovRad)
    val bottomNormal = rotateAroundAxis(forward, up, right, -vfovRad)

    return arrayOf(leftNormal, rightNormal, topNormal, bottomNormal)
}

private fun rotateAroundUp(forward: FloatArray, up: FloatArray, angle: Float): FloatArray {
    val cosA = cos(angle)
    val sinA = sin(angle)
    return floatArrayOf(
        cosA * forward[0] + sinA * up[0],
        cosA * forward[1] + sinA * up[1],
        cosA * forward[2] + sinA * up[2],
    )
}

private fun rotateAroundAxis(forward: FloatArray, up: FloatArray, right: FloatArray, angle: Float): FloatArray {
    val cosA = cos(angle)
    val sinA = sin(angle)
    return floatArrayOf(
        cosA * forward[0] + sinA * right[0],
        cosA * forward[1] + sinA * right[1],
        cosA * forward[2] + sinA * right[2],
    )
}

fun isFaceVisible(face: CubeFace, frustumPlanes: Array<FloatArray>): Boolean {
    return true
}
