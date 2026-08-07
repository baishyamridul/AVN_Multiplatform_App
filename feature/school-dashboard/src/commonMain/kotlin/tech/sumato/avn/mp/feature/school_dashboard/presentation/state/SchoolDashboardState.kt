package tech.sumato.avn.mp.feature.school_dashboard.presentation.state

import tech.sumato.avn.mp.domain.common.model.DistrictModel
import tech.sumato.avn.mp.domain.school.model.SchoolModel
import tech.sumato.avn.mp.feature.school_dashboard.presentation.model.SchoolDetailsUiModel

//sealed interface SchoolDashboardState {
//    data object Loading : SchoolDashboardState
//    data object Idle : SchoolDashboardState
//    data class Error(val message: String) : SchoolDashboardState
//
//
//
//}


data class SchoolDashboardState(
    var schoolsState: SchoolsState = SchoolsState(),
)

data class SchoolsState(
    val isLoading: Boolean = false,
    val schools: List<SchoolModel> = emptyList(),
    val districts: List<DistrictModel> = emptyList(),
    val selectedSchoolId: String? = null,
    val searchQuery: String = "",
    val selectedDistrictId: Int? = null,
    val selectedCategory: String? = null,
    val sortOption: SchoolSortOption = SchoolSortOption.SchoolName,
    val schoolDetails: SchoolDetailsUiModel? = null,
    val isSchoolDetailsLoading: Boolean = false,
)

enum class SchoolSortOption(val label: String) {
    SchoolName("School Name"),

    DistrictName("District"),

    SchoolCategory("School Category"),
}