package tech.sumato.avn.mp.domain.districtDashboard.model

data class DistrictDashboardData(
    val districts: List<DistrictModel>,
    val stats: List<DashboardStatModel>,
    val schoolCategoryList: List<SchoolCategoryModel>,
    val ongoingProjects: OngoingProjectModel,
)
