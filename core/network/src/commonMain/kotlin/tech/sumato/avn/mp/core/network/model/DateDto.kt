package tech.sumato.avn.mp.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class DateDto(
    val human: String,
    val date: String,
    val formatted: String,
)
