package tech.sumato.avn.mp.feature.district_dashboard.presentation

import tech.sumato.avn.mp.domain.districtDashboard.model.DistrictDashboardData

sealed interface DistrictDashboardState {
    data object Loading : DistrictDashboardState
    data class Success(val data: DistrictDashboardData) : DistrictDashboardState
    data class Error(val message: String) : DistrictDashboardState
}
