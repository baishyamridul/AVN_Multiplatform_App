package tech.sumato.avn.mp.feature.school_dashboard.presentation.state

import tech.sumato.avn.mp.domain.school.model.SchoolModel

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
)