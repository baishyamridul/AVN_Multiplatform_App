package tech.sumato.avn.mp.domain.districtDashboard.model

data class OngoingProjectModel(
    val projects: List<DashboardProjectModel>,
    val totalProjects: Int,
)
