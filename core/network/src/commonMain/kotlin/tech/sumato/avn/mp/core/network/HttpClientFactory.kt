package tech.sumato.avn.mp.core.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object HttpClientFactory {

    fun create(
        config: NetworkConfig,
        json: Json,
        tokenProvider: suspend () -> String? = { null },
    ): HttpClient {
        val authPlugin = createClientPlugin("AuthBearer") {
            onRequest { request, _ ->
                val token = tokenProvider()
                if (!token.isNullOrBlank()) {
                    request.headers.append(HttpHeaders.Authorization, "Bearer $token")
                }
            }
        }



        return HttpClient {
            install(authPlugin)

            install(ContentNegotiation) {
                json(json)
            }

            expectSuccess = true

            if (config.enableLogging) {
                install(Logging) {
                    logger = object : io.ktor.client.plugins.logging.Logger {
                        override fun log(message: String) {
                            println(message)
                        }
                    }
                    level = LogLevel.ALL
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
