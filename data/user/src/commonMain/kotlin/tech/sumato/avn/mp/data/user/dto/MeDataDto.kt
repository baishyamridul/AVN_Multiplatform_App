package tech.sumato.avn.mp.data.user.dto

import kotlinx.serialization.Serializable
import tech.sumato.avn.mp.core.network.model.DateDto


@Serializable
data class MeDataDto(
    val id: String,
    val name: String,
    val email: String,
    val role: String? = null,
    val phone: String? = null,
    val photo: String? = null,
    val designation: String? = null,
    val created: DateDto? = null,
    val district: List<DistrictDto>? = null,
)