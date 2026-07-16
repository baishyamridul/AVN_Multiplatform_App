package tech.sumato.kmptemplate.feature.login.presentation

sealed interface LoginEffect {
    data object NavigateToDashboard : LoginEffect
    data class ShowSnackbar(val message: String) : LoginEffect
}
