package tech.sumato.avn.mp.data.user.dto

import kotlinx.serialization.Serializable
import tech.sumato.avn.mp.data.user.remote.CreatedDto
import tech.sumato.avn.mp.data.user.remote.LoginDataDto

@Serializable
data class MeResponseDto(
    val status: Int,
    val message: String,
    val data: LoginDataDto? = null,
)


@Serializable
data class MeDataDto(
    val id: String,
    val name: String,
    val email: String,
    val role: String? = null,
    val phone: String? = null,
    val photo: String? = null,
    val designation: String? = null,
    val created: CreatedDto? = null,
)