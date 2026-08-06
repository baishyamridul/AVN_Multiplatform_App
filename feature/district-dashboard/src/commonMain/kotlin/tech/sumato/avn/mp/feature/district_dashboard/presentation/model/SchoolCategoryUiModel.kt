package tech.sumato.avn.mp.feature.district_dashboard.presentation.model

import kotlinx.serialization.Serializable
import tech.sumato.avn.mp.domain.districtDashboard.model.SchoolCategoryModel


@Serializable
data class SchoolCategoryUiModel(
    val id: String,
    val label: String,
    val classRange: String,
    val schoolCount: Int,
)


fun SchoolCategoryModel.toSchoolCategoryUiModel(): SchoolCategoryUiModel {
    return SchoolCategoryUiModel(
        id = "",
        label = schoolCategory,
        classRange = className,
        schoolCount = totalSchools
    )
}