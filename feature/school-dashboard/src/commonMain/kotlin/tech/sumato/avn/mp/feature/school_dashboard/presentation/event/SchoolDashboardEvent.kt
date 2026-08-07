package tech.sumato.avn.mp.feature.school_dashboard.presentation.event

import tech.sumato.avn.mp.feature.school_dashboard.presentation.state.SchoolSortOption

sealed interface SchoolDashboardEvent {

    data object Back : SchoolDashboardEvent

    data class SelectSchool(val schoolId: String) : SchoolDashboardEvent

    data object ClearSchoolSelection : SchoolDashboardEvent

    data class LoadSchoolDetails(val schoolId: String) : SchoolDashboardEvent

    data object ClearSchoolDetails : SchoolDashboardEvent

    data class UpdateSearchQuery(val query: String) : SchoolDashboardEvent

    data class SelectDistrict(val districtId: Int?) : SchoolDashboardEvent

    data class SelectCategory(val category: String?) : SchoolDashboardEvent

    data class SelectSortOption(val option: SchoolSortOption) : SchoolDashboardEvent

}
