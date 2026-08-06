package tech.sumato.avn.mp.data.school.dto

import kotlinx.serialization.Serializable

@Serializable
data class SchoolDistrictDto(
    val id: Int,
    val name: String
)
