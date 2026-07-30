package tech.sumato.avn.mp.feature.login.presentation

import tech.sumato.avn.mp.domain.user.model.User

sealed interface LoginState {
    data object Idle : LoginState
    data object Loading : LoginState
    data class Success(val user: User) : LoginState
    data class Error(
        val message: String,
        val emailError: String? = null,
        val passwordError: String? = null,
    ) : LoginState
}
