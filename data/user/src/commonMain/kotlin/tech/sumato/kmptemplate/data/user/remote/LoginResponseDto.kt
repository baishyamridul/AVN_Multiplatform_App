package tech.sumato.kmptemplate.data.user.remote

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseDto(
    val token: String,
    val user: UserDto,
)

@Serializable
data class UserDto(
    val id: String,
    val name: String,
    val email: String,
)
