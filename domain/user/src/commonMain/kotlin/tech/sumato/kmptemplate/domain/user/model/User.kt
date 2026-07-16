package tech.sumato.kmptemplate.domain.user.model

data class User(
    val id: String,
    val name: String,
    val email: String,
)

data class AuthResult(
    val token: String,
    val user: User,
)
