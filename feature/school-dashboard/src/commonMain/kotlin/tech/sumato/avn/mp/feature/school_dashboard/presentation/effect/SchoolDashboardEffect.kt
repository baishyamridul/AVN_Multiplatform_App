package tech.sumato.avn.mp.feature.school_dashboard.presentation.effect

sealed interface SchoolDashboardEffect {
    data class ShowSnackbar(val message: String) : SchoolDashboardEffect

    data object NavigateBack : SchoolDashboardEffect
}
