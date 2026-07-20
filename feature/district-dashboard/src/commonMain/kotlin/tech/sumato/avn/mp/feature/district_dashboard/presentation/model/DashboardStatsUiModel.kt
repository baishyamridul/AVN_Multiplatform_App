package tech.sumato.avn.mp.feature.district_dashboard.presentation.model


data class DashboardStatsUiModel(
    val label: String,
    val value: String,
    val supporting: String,
    val valueColor: Long = 0xff666666
)
