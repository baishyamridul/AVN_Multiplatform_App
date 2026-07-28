package tech.sumato.avn.mp.component.panorama.tiles

import tech.sumato.avn.mp.component.panorama.network.PanoramaRepository

class MultiresTileLoader(
    private val repository: PanoramaRepository,
    private val resolver: TilePathResolver,
    private val config: MultiResConfig,
) {
    suspend fun loadLevel(level: Int, face: CubeFace): List<List<ByteArray>> {
        val side = resolver.tilesPerFaceSide(level)
        return (0 until side).map { y ->
            (0 until side).map { x ->
                val url = resolver.tileUrl(level, face, x, y)
                repository.loadTileBytes(url)
            }
        }
    }

    suspend fun loadAllFacesLevel(level: Int): Map<CubeFace, List<List<ByteArray>>> {
        return CubeFace.entries.associateWith { loadLevel(level, it) }
    }

    suspend fun loadProgressive(
        startLevel: Int = 0,
        onLevelUpdate: suspend (Int, Map<CubeFace, List<List<ByteArray>>>) -> Unit,
    ) {
        for (level in startLevel..config.maxLevel) {
            val tiles = loadAllFacesLevel(level)
            onLevelUpdate(level, tiles)
        }
    }
}
