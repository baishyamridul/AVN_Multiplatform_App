package tech.sumato.kmptemplate.feature.login.presentation

import tech.sumato.kmptemplate.domain.user.model.User

sealed interface LoginState {
    data object Idle : LoginState
    data object Loading : LoginState
    data class Success(val user: User) : LoginState
    data class Error(val message: String) : LoginState
}
