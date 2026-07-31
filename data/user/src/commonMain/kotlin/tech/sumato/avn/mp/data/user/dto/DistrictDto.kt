package tech.sumato.avn.mp.data.user.dto

import kotlinx.serialization.Serializable

@Serializable
data class DistrictDto(
    val id: Int,
    val name: String,
)
