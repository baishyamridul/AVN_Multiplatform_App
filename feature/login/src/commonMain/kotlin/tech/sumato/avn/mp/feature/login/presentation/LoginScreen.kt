package tech.sumato.avn.mp.feature.login.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tech.sumato.avn.mp.designsystem.FormFactor
import tech.sumato.avn.mp.designsystem.LocalFormFactor
import tech.sumato.avn.mp.designsystem.components.AppTextField
import tech.sumato.avn.mp.feature.login.presentation.event.LoginEvent
import tech.sumato.avn.mp.feature.login.presentation.screen_variants.LoginScreenExpanded
import tech.sumato.avn.mp.feature.login.presentation.state.LoginState

@Composable
fun LoginScreen(
    email: String,
    password: String,
    state: LoginState,
    onEvent: (LoginEvent) -> Unit,
) {
    val formFactor = LocalFormFactor.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        when (formFactor) {
            FormFactor.Compact -> LoginCompact(email, password, state, onEvent)
            FormFactor.Medium -> LoginWide(email, password, state, onEvent, 480.dp)
            FormFactor.Expanded -> LoginScreenExpanded(email, password, state, onEvent, 560.dp)
        }
    }
}

@Composable
private fun LoginCompact(
    email: String,
    password: String,
    state: LoginState,
    onEvent: (LoginEvent) -> Unit,
) {
    val emailError = (state as? LoginState.Error)?.emailError
    val passwordError = (state as? LoginState.Error)?.passwordError

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        LoginTitle()
        AppTextField(
            value = email,
            onValueChange = { onEvent(LoginEvent.EmailChanged(it)) },
            label = "Email",
            placeholder = "Enter your email",
            error = emailError,
        )
        AppTextField(
            value = password,
            onValueChange = { onEvent(LoginEvent.PasswordChanged(it)) },
            label = "Password",
            placeholder = "Enter your password",
            isSecured = true,
            error = passwordError,
        )
        Spacer(Modifier.height(8.dp))
        LoginButton(state, onEvent)
        LoginError(state)
    }
}

@Composable
private fun LoginWide(
    email: String,
    password: String,
    state: LoginState,
    onEvent: (LoginEvent) -> Unit,
    maxWidth: Dp,
) {
    val emailError = (state as? LoginState.Error)?.emailError
    val passwordError = (state as? LoginState.Error)?.passwordError

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(48.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        Spacer(Modifier.weight(1f))
        Column(
            modifier = Modifier.width(maxWidth),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            LoginTitle()
            AppTextField(
                value = email,
                onValueChange = { onEvent(LoginEvent.EmailChanged(it)) },
                label = "Email",
                placeholder = "Enter your email",
                error = emailError,
            )
            AppTextField(
                value = password,
                onValueChange = { onEvent(LoginEvent.PasswordChanged(it)) },
                label = "Password",
                placeholder = "Enter your password",
                isSecured = true,
                error = passwordError,
            )
            Spacer(Modifier.height(8.dp))
            LoginButton(state, onEvent)
            LoginError(state)
        }
        Spacer(Modifier.weight(1f))
    }
}

@Composable
private fun LoginTitle() {
    Text(
        text = "Login",
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.onBackground,
    )
    Spacer(Modifier.height(8.dp))
}

@Composable
fun LoginButton(
    state: LoginState,
    onEvent: (LoginEvent) -> Unit,
) {
    Button(
        onClick = { onEvent(LoginEvent.LoginClicked) },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(10.dp),
        enabled = state !is LoginState.Loading,
    ) {
        if (state is LoginState.Loading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(24.dp),
            )
        } else {
            Text("Login")
        }
    }
}

@Composable
fun LoginError(state: LoginState) {
    if (state is LoginState.Error && state.emailError == null && state.passwordError == null) {
        Text(
            text = state.message,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}
