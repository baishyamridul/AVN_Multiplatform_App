package tech.sumato.avn.mp.data.user.repository

import tech.sumato.avn.mp.data.user.mapper.AuthMapper
import tech.sumato.avn.mp.data.user.remote.AuthApi
import tech.sumato.avn.mp.data.user.remote.LoginRequestDto
import tech.sumato.avn.mp.domain.user.model.AuthResult
import tech.sumato.avn.mp.domain.user.model.User
import tech.sumato.avn.mp.domain.user.repository.AuthRepository

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val authMapper: AuthMapper,
) : AuthRepository {

    override suspend fun login(email: String, password: String): AuthResult {
        val response = authApi.login(LoginRequestDto(email, password))
        return authMapper.toDomain(response)
    }

    override suspend fun logout() {
    }

    override suspend fun getCurrentUser(): User? {
        return null
    }
}
