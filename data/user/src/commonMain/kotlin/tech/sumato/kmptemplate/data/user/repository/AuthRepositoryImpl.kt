package tech.sumato.kmptemplate.data.user.repository

import tech.sumato.kmptemplate.data.user.mapper.AuthMapper
import tech.sumato.kmptemplate.data.user.remote.AuthApi
import tech.sumato.kmptemplate.data.user.remote.LoginRequestDto
import tech.sumato.kmptemplate.domain.user.model.AuthResult
import tech.sumato.kmptemplate.domain.user.model.User
import tech.sumato.kmptemplate.domain.user.repository.AuthRepository

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
