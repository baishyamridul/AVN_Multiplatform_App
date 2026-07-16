package tech.sumato.kmptemplate.domain.user.usecase

import tech.sumato.kmptemplate.domain.user.model.AuthResult
import tech.sumato.kmptemplate.domain.user.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(email: String, password: String): AuthResult {
        return authRepository.login(email, password)
    }
}
