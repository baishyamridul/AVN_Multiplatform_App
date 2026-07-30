package tech.sumato.avn.mp.domain.user.usecase

import tech.sumato.avn.mp.domain.user.model.AuthResult
import tech.sumato.avn.mp.domain.user.model.User
import tech.sumato.avn.mp.domain.user.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(email: String, password: String): AuthResult {
        return authRepository.login(email, password)
    }

    suspend fun getCurrentUser(): User? {
        return authRepository.getCurrentUser()
    }
}
