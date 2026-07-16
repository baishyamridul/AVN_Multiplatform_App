package tech.sumato.kmptemplate.feature.dashboard.presentation

sealed interface DashboardEffect {
    data class ShowSnackbar(val message: String) : DashboardEffect
}
