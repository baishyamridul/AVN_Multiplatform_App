package tech.sumato.avn.mp.domain.user.model

data class LoginValidationResult(
    val isValid: Boolean,
    val emailError: String? = null,
    val passwordError: String? = null,
)
