package tech.sumato.avn.mp.feature.login.presentation

sealed interface LoginEffect {
    data object NavigateToDashboard : LoginEffect
    data class ShowSnackbar(val message: String) : LoginEffect
}
