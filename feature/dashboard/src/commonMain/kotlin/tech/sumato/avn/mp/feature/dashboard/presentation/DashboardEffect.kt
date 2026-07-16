package tech.sumato.avn.mp.feature.dashboard.presentation

sealed interface DashboardEffect {
    data class ShowSnackbar(val message: String) : DashboardEffect
}
