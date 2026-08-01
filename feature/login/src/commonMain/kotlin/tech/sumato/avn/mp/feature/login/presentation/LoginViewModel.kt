package tech.sumato.avn.mp.feature.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import tech.sumato.avn.mp.core.navigation.MviViewModel
import tech.sumato.avn.mp.domain.user.model.LoginException
import tech.sumato.avn.mp.domain.user.model.LoginValidator
import tech.sumato.avn.mp.domain.user.usecase.LoginUseCase
import tech.sumato.avn.mp.feature.login.presentation.effect.LoginEffect
import tech.sumato.avn.mp.feature.login.presentation.event.LoginEvent
import tech.sumato.avn.mp.feature.login.presentation.state.LoginState

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
) : ViewModel(), MviViewModel<LoginState, LoginEffect> {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _state = MutableStateFlow<LoginState>(LoginState.Idle)
    override val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _effects = Channel<LoginEffect>(Channel.BUFFERED)
    override val effects = _effects.receiveAsFlow()

    init {
        checkStoredSession()
    }

    private fun checkStoredSession() {
        viewModelScope.launch {
            val user = loginUseCase.getCurrentUser()
            if (user != null) {
                _state.value = LoginState.Success(user)
                _effects.send(LoginEffect.NavigateToDashboard)
            }
        }
    }

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                _email.value = event.email
                clearEmailError()
            }
            is LoginEvent.PasswordChanged -> {
                _password.value = event.password
                clearPasswordError()
            }
            LoginEvent.LoginClicked -> {
                login()
            }
            LoginEvent.Retry -> {
                login()
            }
            is LoginEvent.SessionExpired -> {
                _email.value = ""
                _password.value = ""
                _state.value = LoginState.Error(message = event.message)
            }
        }
    }

    private fun login() {
        val current = _state.value
        if (current is LoginState.Loading) return

        val validation = LoginValidator.validate(_email.value, _password.value)
        if (!validation.isValid) {
            _state.value = LoginState.Error(
                message = "Please fix the errors below",
                emailError = validation.emailError,
                passwordError = validation.passwordError,
            )
            return
        }

        viewModelScope.launch {
            _state.value = LoginState.Loading
            try {
                val result = loginUseCase(_email.value, _password.value)
                _state.value = LoginState.Success(result.user)
                _effects.send(LoginEffect.NavigateToDashboard)
            } catch (e: LoginException) {
                e.printStackTrace()
                _state.value = LoginState.Error(
                    message = e.message,
                    emailError = e.fieldErrors?.get("email")?.firstOrNull(),
                    passwordError = e.fieldErrors?.get("password")?.firstOrNull(),
                )
            } catch (e: Exception) {
                _state.value = LoginState.Error(message = e.message ?: "Login failed")
                _effects.send(LoginEffect.ShowSnackbar("Login failed: ${e.message}"))
            }
        }
    }

    private fun clearEmailError() {
        val current = _state.value
        if (current is LoginState.Error && current.emailError != null) {
            _state.value = current.copy(
                emailError = null,
                message = if (current.passwordError == null) "" else current.message,
            )
        }
    }

    private fun clearPasswordError() {
        val current = _state.value
        if (current is LoginState.Error && current.passwordError != null) {
            _state.value = current.copy(
                passwordError = null,
                message = if (current.emailError == null) "" else current.message,
            )
        }
    }
}
