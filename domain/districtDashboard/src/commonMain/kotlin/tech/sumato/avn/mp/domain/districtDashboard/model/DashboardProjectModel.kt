package tech.sumato.avn.mp.domain.districtDashboard.model

import tech.sumato.avn.mp.domain.common.model.DateModel

data class DashboardProjectModel(
    val id: String,
    val projectName: String,
    val progressPercent: Int,
    val districtName: String,
    val updatedAt: DateModel,
)
