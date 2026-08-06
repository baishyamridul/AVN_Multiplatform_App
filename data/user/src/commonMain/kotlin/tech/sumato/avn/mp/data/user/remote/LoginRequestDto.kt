package tech.sumato.avn.mp.data.user.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(
    val email: String,
    val password: String,
    @SerialName("device_name")
    val deviceName: String = "mobile",
)
