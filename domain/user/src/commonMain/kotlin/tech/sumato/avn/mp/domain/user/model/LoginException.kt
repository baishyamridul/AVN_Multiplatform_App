package tech.sumato.avn.mp.domain.user.model

class LoginException(
    override val message: String,
    val fieldErrors: Map<String, List<String>>? = null,
) : Exception(message)
