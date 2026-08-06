package tech.sumato.avn.mp.domain.user.repository

import tech.sumato.avn.mp.domain.user.model.AuthResult
import tech.sumato.avn.mp.domain.user.model.User
import tech.sumato.avn.mp.domain.user.model.UserDetailsModel

interface AuthRepository {
    suspend fun login(email: String, password: String): AuthResult
    suspend fun getUserDetails(): UserDetailsModel
    suspend fun getStoredUserDetails(): UserDetailsModel?
    suspend fun logout()
    suspend fun getCurrentUser(): User?
}
