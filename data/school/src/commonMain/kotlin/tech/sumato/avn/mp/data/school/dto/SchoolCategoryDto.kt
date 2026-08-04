package tech.sumato.avn.mp.data.school.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class SchoolCategoryDto(
    val key: String,
    val name: String,
    @SerialName("class")
    val classRange: String,
)
