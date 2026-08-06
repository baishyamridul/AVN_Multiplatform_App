package tech.sumato.avn.mp.domain.districtDashboard.model

import kotlinx.serialization.SerialName

data class DashboardProjectCategoryData(
    val id: String,
    val categoryName: String,
    val completedPercent: Float,
    val total: Int,
    val totalCompleted: Int,
    val totalOngoing: Int,
)
