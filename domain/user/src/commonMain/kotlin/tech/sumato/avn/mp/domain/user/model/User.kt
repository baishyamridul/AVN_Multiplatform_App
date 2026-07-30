package tech.sumato.avn.mp.domain.user.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val role: String? = null,
    val phone: String? = null,
    val photo: String? = null,
    val designation: String? = null,
)

data class AuthResult(
    val tokenType: String,
    val accessToken: String,
    val user: User,
)
