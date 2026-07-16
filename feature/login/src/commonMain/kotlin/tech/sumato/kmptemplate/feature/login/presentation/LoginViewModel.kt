package tech.sumato.kmptemplate.feature.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import tech.sumato.kmptemplate.core.navigation.MviViewModel
import tech.sumato.kmptemplate.domain.user.usecase.LoginUseCase

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

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                _email.value = event.email
            }
            is LoginEvent.PasswordChanged -> {
                _password.value = event.password
            }
            LoginEvent.LoginClicked -> {
                login()
            }
            LoginEvent.Retry -> {
                login()
            }
        }
    }

    private fun login() {
        viewModelScope.launch {
            _state.value = LoginState.Loading
            try {
                val result = loginUseCase(_email.value, _password.value)
                _state.value = LoginState.Success(result.user)
                _effects.send(LoginEffect.NavigateToDashboard)
            } catch (e: Exception) {
                _state.value = LoginState.Error(e.message ?: "Login failed")
                _effects.send(LoginEffect.ShowSnackbar("Login failed: ${e.message}"))
            }
        }
    }
}
