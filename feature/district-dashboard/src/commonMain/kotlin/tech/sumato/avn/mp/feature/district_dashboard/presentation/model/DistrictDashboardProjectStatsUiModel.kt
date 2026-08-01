package tech.sumato.avn.mp.feature.district_dashboard.presentation.model

import tech.sumato.avn.mp.domain.districtDashboard.model.DashboardProjectCategoryData


data class DistrictDashboardProjectStatsUiModel(
    val id: String,
    val categoryName: String,
    val completedPercent: Float,
    val total: Int,
    val totalCompleted: Int,
    val totalOngoing: Int,
)


fun DashboardProjectCategoryData.toUiModel(): DistrictDashboardProjectStatsUiModel {
    return DistrictDashboardProjectStatsUiModel(
        id = id,
        categoryName = categoryName,
        completedPercent = completedPercent,
        total = total,
        totalCompleted = totalCompleted,
        totalOngoing = totalOngoing,
    )
}