package tech.sumato.avn.mp.component.panorama.tiles

import kotlin.math.pow

data class PannellumConfig(
    val hfov: Float,
    val type: String,
    val multiRes: MultiResConfig,
)

data class MultiResConfig(
    val path: String,
    val fallbackPath: String,
    val extension: String,
    val tileResolution: Int,
    val maxLevel: Int,
    val cubeResolution: Int,
)

class TilePathResolver(private val baseUrl: String, private val config: MultiResConfig) {

    fun tileUrl(level: Int, face: CubeFace, x: Int, y: Int): String {
        val resolved = config.path
            .replace("%l", level.toString())
            .replace("%s", face.id)
            .replace("%y", y.toString())
            .replace("%x", x.toString())
        return joinUrl(baseUrl, resolved + "." + config.extension)
    }

    fun fallbackUrl(face: CubeFace): String {
        val resolved = config.fallbackPath.replace("%s", face.id)
        return joinUrl(baseUrl, resolved + "." + config.extension)
    }

    fun tilesPerFaceSide(level: Int): Int {
        val baseTiles = config.cubeResolution / config.tileResolution
        return (baseTiles * 2.0.pow((level - 1).toDouble())).toInt()
            .coerceAtLeast(1)
    }

    fun totalTilesPerFace(level: Int): Int {
        val side = tilesPerFaceSide(level)
        return side * side
    }

    private fun joinUrl(base: String, path: String): String {
        val baseClean = base.trimEnd('/')
        val pathClean = path.trimStart('/')
        return "$baseClean/$pathClean"
    }
}
