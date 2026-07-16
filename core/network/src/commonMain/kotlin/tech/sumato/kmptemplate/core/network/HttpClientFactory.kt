package tech.sumato.kmptemplate.core.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object HttpClientFactory {

    fun create(config: NetworkConfig): HttpClient {
        return HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    prettyPrint = false
                    encodeDefaults = true
                })
            }

            if (config.enableLogging) {
                install(Logging) {
                    logger = object : io.ktor.client.plugins.logging.Logger {
                        override fun log(message: String) {
                            println(message)
                        }
                    }
                    level = LogLevel.HEADERS
                }
            }

            install(HttpTimeout) {
                requestTimeoutMillis = config.requestTimeoutMs
                connectTimeoutMillis = config.connectTimeoutMs
                socketTimeoutMillis = config.socketTimeoutMs
            }

            defaultRequest {
                url(config.baseUrl)
                contentType(ContentType.Application.Json)
                config.headers.forEach { (key, value) ->
                    headers.append(key, value)
                }
            }
        }
    }
}
