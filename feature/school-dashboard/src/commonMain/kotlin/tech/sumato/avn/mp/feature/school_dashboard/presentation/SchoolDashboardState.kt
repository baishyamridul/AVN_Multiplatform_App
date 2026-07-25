package tech.sumato.avn.mp.feature.school_dashboard.presentation

sealed interface SchoolDashboardState {
    data object Loading : SchoolDashboardState
    data object Idle : SchoolDashboardState
    data class Error(val message: String) : SchoolDashboardState
}
