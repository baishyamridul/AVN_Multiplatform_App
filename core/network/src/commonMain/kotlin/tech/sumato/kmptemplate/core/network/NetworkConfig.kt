package tech.sumato.kmptemplate.core.network

data class NetworkConfig(
    val baseUrl: String,
    val connectTimeoutMs: Long = 10_000L,
    val requestTimeoutMs: Long = 30_000L,
    val socketTimeoutMs: Long = 30_000L,
    val headers: Map<String, String> = emptyMap(),
    val enableLogging: Boolean = true,
)
