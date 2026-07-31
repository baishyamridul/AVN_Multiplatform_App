package tech.sumato.avn.mp.data.user.repository

import io.ktor.client.call.body
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import tech.sumato.avn.mp.core.datastore.UserPreferencesStorage
import tech.sumato.avn.mp.core.network.model.DateDto
import tech.sumato.avn.mp.core.network.model.ErrorResponseWrapper
import tech.sumato.avn.mp.data.user.dto.DistrictDto
import tech.sumato.avn.mp.data.user.dto.MeDataDto
import tech.sumato.avn.mp.data.user.mapper.AuthMapper
import tech.sumato.avn.mp.data.user.mapper.MeMapper
import tech.sumato.avn.mp.data.user.remote.AuthApi
import tech.sumato.avn.mp.data.user.remote.LoginRequestDto
import tech.sumato.avn.mp.domain.user.model.AuthResult
import tech.sumato.avn.mp.domain.user.model.LoginException
import tech.sumato.avn.mp.domain.user.model.User
import tech.sumato.avn.mp.domain.user.model.UserDetailsModel
import tech.sumato.avn.mp.domain.user.repository.AuthRepository

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val authMapper: AuthMapper,
    private val meMapper: MeMapper,
    private val json: Json,
    private val storage: UserPreferencesStorage,
) : AuthRepository {

    override suspend fun login(email: String, password: String): AuthResult {
        val result = try {
            val response = authApi.login(LoginRequestDto(email, password))
            authMapper.toDomain(response)
        } catch (e: io.ktor.client.plugins.ClientRequestException) {
            val errorBody = try {
                json.decodeFromString<ErrorResponseWrapper>(e.response.bodyAsText())
            } catch (_: Exception) {
                ErrorResponseWrapper(status = e.response.status.value, message = e.message)
            }
            throw LoginException(
                message = errorBody.message,
                fieldErrors = errorBody.errors,
            )
        }
        storage.saveAuth(
            accessToken = result.accessToken,
            tokenType = result.tokenType,
            userId = result.user.id,
            name = result.user.name,
            email = result.user.email,
            role = result.user.role,
            phone = result.user.phone,
            photo = result.user.photo,
            designation = result.user.designation,
        )

        try {
            val meData = fetchMeData()
            val details = meMapper.toDomain(meData)
            storage.saveUser(
                userId = details.id,
                name = details.name,
                email = details.email,
                role = details.role,
                phone = details.phone,
                photo = details.photo,
                designation = details.designation,
                createdJson = meData.created?.let { json.encodeToString(it) },
                districtsJson = meData.district?.let { json.encodeToString(it) },
            )
        } catch (e: Exception) {
            logout()
            throw LoginException(message = e.message ?: "Failed to load user data")
        }
        return result
    }

    override suspend fun getUserDetails(): UserDetailsModel {
        return meMapper.toDomain(fetchMeData())
    }

    override suspend fun getStoredUserDetails(): UserDetailsModel? {
        val stored = storage.getAuth() ?: return null
        val created = stored.createdJson?.let { json.decodeFromString<DateDto>(it) }
        val districts = stored.districtsJson
            ?.let { json.decodeFromString<List<DistrictDto>>(it) }
            .orEmpty()
        return UserDetailsModel(
            id = stored.userId,
            name = stored.name,
            email = stored.email,
            role = stored.role,
            phone = stored.phone,
            photo = stored.photo,
            designation = stored.designation,
            created = created?.let { meMapper.toDomain(it) },
            districts = districts.map { meMapper.toDomain(it) },
        )
    }

    private suspend fun fetchMeData(): MeDataDto {
        val response = authApi.me()
        return response.data ?: throw IllegalStateException("User details are missing")
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
        val stored = storage.getAuth() ?: return null
        return User(
            id = stored.userId,
            name = stored.name,
            email = stored.email,
            role = stored.role,
            phone = stored.phone,
            photo = stored.photo,
            designation = stored.designation,
        )
    }
}
