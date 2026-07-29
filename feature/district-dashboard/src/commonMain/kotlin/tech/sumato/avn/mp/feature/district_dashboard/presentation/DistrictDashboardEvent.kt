package tech.sumato.avn.mp.feature.district_dashboard.presentation

sealed interface DistrictDashboardEvent {
    data object LoadData : DistrictDashboardEvent
    data object Retry : DistrictDashboardEvent
}
