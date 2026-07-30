package tech.sumato.avn.mp.domain.districtDashboard.model

data class DashboardProjectModel(
    val id: String,
    val projectName: String,
    val progressPercent: Int,
    val districtName: String,
    val updatedAt: UpdatedAtModel,
)
