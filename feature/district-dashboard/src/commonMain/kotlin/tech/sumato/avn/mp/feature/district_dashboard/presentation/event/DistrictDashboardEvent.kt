package tech.sumato.avn.mp.feature.district_dashboard.presentation.event

sealed interface DistrictDashboardEvent {


    data class LoadData(val districtId: Int = -1) :
        DistrictDashboardEvent

    data class Retry(val districtId: Int = -1) :
        DistrictDashboardEvent

    data object Logout :
        DistrictDashboardEvent

    data class Navigate(val route: String) : DistrictDashboardEvent


}