package tech.sumato.avn.mp.component.panorama.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import tech.sumato.avn.mp.component.panorama.tiles.MultiResConfig
import tech.sumato.avn.mp.component.panorama.tiles.PannellumConfig
import tech.sumato.avn.mp.component.panorama.tiles.TilePathResolver

@Serializable
private data class PannellumConfigJson(
    val hfov: Double = 100.0,
    val type: String = "multires",
    @SerialName("multiRes")
    val multiRes: MultiResConfigJson? = null,
    @SerialName("multiResolution")
    val multiResolution: MultiResConfigJson? = null,
) {
    val effectiveMultiRes: MultiResConfigJson
        get() = multiRes ?: multiResolution ?: MultiResConfigJson()
}

@Serializable
private data class MultiResConfigJson(
    val path: String = "/%l/%s%y_%x",
    val fallbackPath: String = "/fallback/%s",
    val extension: String = "jpg",
    val tileResolution: Int = 512,
    val maxLevel: Int = 3,
    val cubeResolution: Int = 1888,
)

class PanoramaRepository(private val httpClient: HttpClient) {

    private val json = Json { ignoreUnknownKeys = true }

    suspend fun loadConfig(configUrl: String): PannellumConfig {
        val jsonStr: String = httpClient.get(configUrl).body()
        val parsed = json.decodeFromString<PannellumConfigJson>(jsonStr)
        val m = parsed.effectiveMultiRes
        return PannellumConfig(
            hfov = parsed.hfov.toFloat(),
            type = parsed.type,
            multiRes = MultiResConfig(
                path = m.path,
                fallbackPath = m.fallbackPath,
                extension = m.extension,
                tileResolution = m.tileResolution,
                maxLevel = m.maxLevel,
                cubeResolution = m.cubeResolution,
            ),
        )
    }

    suspend fun loadTileBytes(url: String): ByteArray {
        return httpClient.get(url).body()
    }

    fun createResolver(configUrl: String, config: PannellumConfig): TilePathResolver {
        val baseUrl = configUrl.substringBeforeLast("/")
        return TilePathResolver(baseUrl, config.multiRes)
    }
}
