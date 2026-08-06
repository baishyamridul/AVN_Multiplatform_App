package tech.sumato.avn.mp.domain.user.usecase

import tech.sumato.avn.mp.domain.user.model.UserDetailsModel
import tech.sumato.avn.mp.domain.user.repository.AuthRepository

class GetStoredUserDetailsUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(): UserDetailsModel? {
        return authRepository.getStoredUserDetails()
    }
}
