package tech.sumato.avn.mp.feature.district_dashboard.presentation.model

import androidx.compose.ui.graphics.Color
import tech.sumato.avn.mp.domain.districtDashboard.model.DashboardProjectModel
import tech.sumato.avn.mp.domain.districtDashboard.model.OngoingProjectModel

data class OngoingProjectStatsUiModel(
    val id: String,
    val name: String,
    val region: String,
    val progress: Double = 0.0,
)


fun OngoingProjectStatsUiModel.progressColors(darkTheme: Boolean): Color {
    return when (darkTheme) {
        true -> {
            when {
                progress < 30 -> Color(0xffef4444)
                progress < 55 -> Color(0xfff97316)
                progress < 85 -> Color(0xff0ea5e9)
                else -> Color(0xff22c55e)
            }
        }

        false -> {
            when {
                progress < 30 -> Color.Red.copy(alpha = 0.85f)
                progress < 55 -> Color(0xffb45309)
                progress < 85 -> Color(0xff0284c7)
                else -> Color(0xff65a30d)
            }
        }
    }


}


fun DashboardProjectModel.toOngoingProjectStatsUiModel(): OngoingProjectStatsUiModel {
    return OngoingProjectStatsUiModel(
        id = id,
        name = projectName,
        region = districtName,
        progress = progressPercent.toDouble(),
    )
}
