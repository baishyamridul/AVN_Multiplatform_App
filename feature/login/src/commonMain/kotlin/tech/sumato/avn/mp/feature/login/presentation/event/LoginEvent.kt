package tech.sumato.avn.mp.feature.login.presentation.event

sealed interface LoginEvent {
    data class EmailChanged(val email: String) : LoginEvent
    data class PasswordChanged(val password: String) : LoginEvent
    data object LoginClicked : LoginEvent
    data object Retry : LoginEvent
    data class SessionExpired(val message: String) : LoginEvent
}
