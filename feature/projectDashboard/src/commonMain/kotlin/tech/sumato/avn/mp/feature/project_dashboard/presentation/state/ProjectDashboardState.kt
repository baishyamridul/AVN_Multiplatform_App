package tech.sumato.avn.mp.feature.project_dashboard.presentation.state

sealed interface ProjectDashboardState {
    data object Loading : ProjectDashboardState
    data object Idle : ProjectDashboardState
    data class Error(val message: String) : ProjectDashboardState
}
