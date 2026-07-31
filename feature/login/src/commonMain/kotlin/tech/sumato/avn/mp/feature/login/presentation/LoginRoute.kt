package tech.sumato.avn.mp.feature.login.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import tech.sumato.avn.mp.core.navigation.BaseRoute

@Composable
fun LoginRoute(
    onNavigateToDashboard: () -> Unit,
    sessionExpiredMessage: String? = null,
) {
    BaseRoute<LoginViewModel, LoginState, LoginEffect>(
        onEffect = { effect ->
            when (effect) {
                is LoginEffect.NavigateToDashboard -> onNavigateToDashboard()
                is LoginEffect.ShowSnackbar -> { }
            }
        },
        content = { state ->
            val email by email.collectAsState()
            val password by password.collectAsState()
            LaunchedEffect(sessionExpiredMessage) {
                sessionExpiredMessage?.let { onEvent(LoginEvent.SessionExpired(it)) }
            }
            LoginScreen(
                email = email,
                password = password,
                state = state,
                onEvent = ::onEvent,
            )
        },
    )
}
