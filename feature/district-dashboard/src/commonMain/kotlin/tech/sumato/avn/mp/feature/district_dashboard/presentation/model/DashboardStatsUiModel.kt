package tech.sumato.avn.mp.feature.district_dashboard.presentation.model

import tech.sumato.avn.mp.domain.districtDashboard.model.SchoolCategoryModel


data class DashboardStatsUiModel(
    val label: String,
    val value: String,
    val supporting: String,
    val valueColor: Long = 0xff666666
)


