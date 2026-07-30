package tech.sumato.avn.mp.data.user.repository

import io.ktor.client.call.body
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import tech.sumato.avn.mp.core.datastore.UserPreferencesStorage
import tech.sumato.avn.mp.data.user.mapper.AuthMapper
import tech.sumato.avn.mp.data.user.remote.AuthApi
import tech.sumato.avn.mp.data.user.remote.ErrorResponseDto
import tech.sumato.avn.mp.data.user.remote.LoginRequestDto
import tech.sumato.avn.mp.domain.user.model.AuthResult
import tech.sumato.avn.mp.domain.user.model.LoginException
import tech.sumato.avn.mp.domain.user.model.User
import tech.sumato.avn.mp.domain.user.repository.AuthRepository

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val authMapper: AuthMapper,
    private val json: Json,
    private val storage: UserPreferencesStorage,
) : AuthRepository {

    override suspend fun login(email: String, password: String): AuthResult {
        val result = try {
            val response = authApi.login(LoginRequestDto(email, password))
            authMapper.toDomain(response)
        } catch (e: io.ktor.client.plugins.ClientRequestException) {
            val errorBody = try {
                json.decodeFromString<ErrorResponseDto>(e.response.bodyAsText())
            } catch (_: Exception) {
                ErrorResponseDto(status = e.response.status.value, message = e.message)
            }
            throw LoginException(
                message = errorBody.message,
                fieldErrors = errorBody.errors,
            )
        }
        storage.saveAuth(result)
        return result
    }

    override suspend fun logout() {
        try {
            authApi.logout()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        storage.clear()
    }

    override suspend fun getCurrentUser(): User? {
        return storage.getAuth()?.user
    }
}
