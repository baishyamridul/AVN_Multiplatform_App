package tech.sumato.avn.mp.feature.district_dashboard.presentation

sealed interface DistrictDashboardState {
    data object Loading : DistrictDashboardState
    data object Idle : DistrictDashboardState
    data class Error(val message: String) : DistrictDashboardState
}
