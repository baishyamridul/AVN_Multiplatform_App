package tech.sumato.avn.mp.feature.district_dashboard.presentation.model

import tech.sumato.avn.mp.domain.districtDashboard.model.DistrictModel

data class DistrictUiModel(
    val id: Int,
    val name: String,
) {

    override fun toString(): String {
        return name
    }

}


fun DistrictModel.toDistrictUiModel(): DistrictUiModel {
    return DistrictUiModel(id = id, name = name)
}



