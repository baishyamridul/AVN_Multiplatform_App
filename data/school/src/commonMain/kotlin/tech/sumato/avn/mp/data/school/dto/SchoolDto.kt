package tech.sumato.avn.mp.data.school.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SchoolDto(
    val id: String,
    val name: String,
    val category: SchoolCategoryDto? = null,
    @SerialName("lat")
    val latitude: Double? = null,
    @SerialName("lng")
    val longitude: Double? = null,
    val district: SchoolDistrictDto
)
