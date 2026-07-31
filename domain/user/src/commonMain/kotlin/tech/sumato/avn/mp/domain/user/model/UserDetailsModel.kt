package tech.sumato.avn.mp.domain.user.model

import tech.sumato.avn.mp.domain.common.model.DateModel
import tech.sumato.avn.mp.domain.common.model.DistrictModel

data class UserDetailsModel(
    val id: String,
    val name: String,
    val email: String,
    val role: String? = null,
    val phone: String? = null,
    val photo: String? = null,
    val designation: String? = null,
    val created: DateModel? = null,
    val districts: List<DistrictModel> = emptyList(),
)
