package tech.sumato.avn.mp.data.user.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseDto(
    val status: Int,
    val message: String,
    val data: LoginDataDto? = null,
)

@Serializable
data class LoginDataDto(
    @SerialName("token_type")
    val tokenType: String,
    @SerialName("access_token")
    val accessToken: String,
    val user: UserDataDto,
)

@Serializable
data class UserDataDto(
    val type: String,
    val id: String,
    val attributes: UserAttributesDto,
)

@Serializable
data class UserAttributesDto(
    val name: String,
    val email: String,
    val role: String? = null,
    val phone: String? = null,
    val photo: String? = null,
    val designation: String? = null,
    val created: CreatedDto? = null,
)

@Serializable
data class CreatedDto(
    val human: String,
    val date: String,
    val formatted: String,
)

@Serializable
data class ErrorResponseDto(
    val status: Int,
    val message: String,
    val errors: Map<String, List<String>>? = null,
)
