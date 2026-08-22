package tech.sumato.avn.mp.feature.project_dashboard.presentation.effect

sealed interface ProjectDashboardEffect {
    data class ShowSnackbar(val message: String) : ProjectDashboardEffect
}
