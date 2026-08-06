package tech.sumato.avn.mp.data.user.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import tech.sumato.avn.mp.core.network.model.SuccessResponseWrapper
import tech.sumato.avn.mp.data.user.dto.MeDataDto

class AuthApi(
    private val httpClient: HttpClient,
) {
    suspend fun login(request: LoginRequestDto): SuccessResponseWrapper<LoginDataDto> {
        return httpClient.post("login") {
            setBody(request)
        }.body()
    }

    suspend fun me(): SuccessResponseWrapper<MeDataDto> {
        return httpClient.get("me") {
        }.body()
    }


    suspend fun logout() {
        httpClient.post("logout")
    }

}
