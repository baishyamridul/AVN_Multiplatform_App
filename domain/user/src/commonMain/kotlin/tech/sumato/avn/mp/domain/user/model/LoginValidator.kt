package tech.sumato.avn.mp.domain.user.model

private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

object LoginValidator {
    fun validate(email: String, password: String): LoginValidationResult {
        var emailError: String? = null
        var passwordError: String? = null

        if (email.isBlank()) {
            emailError = "Email is required"
        } else if (!emailRegex.matches(email.trim())) {
            emailError = "Enter a valid email address"
        }

        if (password.isBlank()) {
            passwordError = "Password is required"
        }

        return LoginValidationResult(
            isValid = emailError == null && passwordError == null,
            emailError = emailError,
            passwordError = passwordError,
        )
    }
}
