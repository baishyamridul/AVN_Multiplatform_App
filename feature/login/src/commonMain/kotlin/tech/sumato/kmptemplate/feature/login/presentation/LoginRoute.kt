package tech.sumato.kmptemplate.feature.login.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import tech.sumato.kmptemplate.core.navigation.BaseRoute

@Composable
fun LoginRoute(
    onNavigateToDashboard: () -> Unit,
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
            LoginScreen(
                email = email,
                password = password,
                state = state,
                onEvent = ::onEvent,
            )
        },
    )
}
