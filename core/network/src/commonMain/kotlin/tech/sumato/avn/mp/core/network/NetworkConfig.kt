package tech.sumato.avn.mp.core.network

data class NetworkConfig(
    val apiBaseUrl: String,
    val connectTimeoutMs: Long = 10_000L,
    val requestTimeoutMs: Long = 30_000L,
    val socketTimeoutMs: Long = 30_000L,
    val headers: Map<String, String> = emptyMap(),
    val enableLogging: Boolean = true,
)

data class BaseUrls(
    val baseUrl: String,
    val apiBaseUrl: String,
)
