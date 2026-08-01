package tech.sumato.avn.mp.domain.districtDashboard.model

import tech.sumato.avn.mp.domain.common.model.DistrictModel

data class DistrictDashboardData(
    val districts: List<DistrictModel>,
    val stats: List<DashboardStatModel>,
    val schoolCategoryList: List<SchoolCategoryModel>,
    val ongoingProjects: OngoingProjectModel,
    val projectStats: DistrictDashboardProjectStatsData,
)
