package tech.sumato.avn.mp.feature.district_dashboard.presentation

sealed interface DistrictDashboardEffect {
    data class ShowSnackbar(val message: String) : DistrictDashboardEffect
}
