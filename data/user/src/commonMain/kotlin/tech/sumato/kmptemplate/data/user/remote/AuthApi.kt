package tech.sumato.kmptemplate.data.user.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class AuthApi(
    private val httpClient: HttpClient,
) {
    suspend fun login(request: LoginRequestDto): LoginResponseDto {
        return httpClient.post("auth/login") {
            setBody(request)
        }.body()
    }
}
