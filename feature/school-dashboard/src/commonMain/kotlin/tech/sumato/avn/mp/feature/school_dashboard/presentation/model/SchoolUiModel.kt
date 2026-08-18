package tech.sumato.avn.mp.feature.school_dashboard.presentation.model

import tech.sumato.avn.mp.domain.common.model.DistrictModel
import tech.sumato.avn.mp.domain.school.model.SchoolCategoryModel
import tech.sumato.avn.mp.domain.school.model.SchoolModel


data class SchoolUiModel(
    val id: String,
    val name: String,
    val udise: String,
    val category: SchoolCategoryUiModel? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val districtModel: DistrictUiModel,
) {
    fun hasLocation(): Boolean {
        return latitude != null && longitude != null
    }
}


fun SchoolModel.toUiModel(): SchoolUiModel {
    return SchoolUiModel(
        id = id,
        name = name,
        udise = udise,
        category = category?.toUiModel(),
        latitude = latitude,
        longitude = longitude,
        districtModel = district.toUiModel()
    )
}

