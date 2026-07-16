package tech.sumato.kmptemplate.domain.user.repository

import tech.sumato.kmptemplate.domain.user.model.AuthResult
import tech.sumato.kmptemplate.domain.user.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): AuthResult
    suspend fun logout()
    suspend fun getCurrentUser(): User?
}
