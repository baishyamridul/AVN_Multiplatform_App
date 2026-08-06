package tech.sumato.avn.mp.domain.districtDashboard.model

data class DistrictDashboardProjectStatsData(
    val projects: List<DashboardProjectCategoryData>,
    val totalProjects: Int

)
