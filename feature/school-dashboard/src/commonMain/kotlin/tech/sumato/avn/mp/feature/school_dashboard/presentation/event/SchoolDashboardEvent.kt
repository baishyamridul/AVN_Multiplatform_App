package tech.sumato.avn.mp.feature.school_dashboard.presentation.event

import tech.sumato.avn.mp.feature.school_dashboard.presentation.state.SchoolSortOption

sealed interface SchoolDashboardEvent {

    data object Back : SchoolDashboardEvent

    data class SelectSchool(val schoolId: String) : SchoolDashboardEvent

    data object ClearSchoolSelection : SchoolDashboardEvent

    data class UpdateSearchQuery(val query: String) : SchoolDashboardEvent

    data class SelectDistrict(val district: String?) : SchoolDashboardEvent

    data class SelectSortOption(val option: SchoolSortOption) : SchoolDashboardEvent

}
