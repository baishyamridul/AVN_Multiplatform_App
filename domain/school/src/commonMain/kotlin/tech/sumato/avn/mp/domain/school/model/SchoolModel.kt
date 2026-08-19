package tech.sumato.avn.mp.domain.school.model

import tech.sumato.avn.mp.domain.common.model.DistrictModel

data class SchoolModel(
    val id: String,
    val name: String,
    val udise: String,
    val category: SchoolCategoryModel? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val district: DistrictModel,
    val qrUrl: String,
    val goldenJubilee: Boolean,
    val pmShri: Boolean,
)
