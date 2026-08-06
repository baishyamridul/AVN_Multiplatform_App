package tech.sumato.avn.mp.feature.school_dashboard.presentation.model

import tech.sumato.avn.mp.domain.common.model.DistrictModel

data class DistrictUiModel(
    val id: Int,
    val name: String
)


fun DistrictModel.toUiModel() : DistrictUiModel {
    return DistrictUiModel(id = id, name = name)
}
