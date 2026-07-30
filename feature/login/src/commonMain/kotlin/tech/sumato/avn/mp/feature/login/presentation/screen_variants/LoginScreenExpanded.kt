package tech.sumato.avn.mp.feature.login.presentation.screen_variants

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import avnmultiplatformapp.designsystem.generated.resources.app_logo
import avnmultiplatformapp.designsystem.generated.resources.Res as DesignSystemRes
import org.jetbrains.compose.resources.painterResource
import tech.sumato.avn.mp.designsystem.components.AppTextField
import tech.sumato.avn.mp.feature.login.presentation.LoginButton
import tech.sumato.avn.mp.feature.login.presentation.LoginError
import tech.sumato.avn.mp.feature.login.presentation.LoginEvent
import tech.sumato.avn.mp.feature.login.presentation.LoginState


@Composable
fun LoginScreenExpanded(
    email: String,
    password: String,
    state: LoginState,
    onEvent: (LoginEvent) -> Unit,
    dp: Dp
) {
    val emailError = (state as? LoginState.Error)?.emailError
    val passwordError = (state as? LoginState.Error)?.passwordError

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalArrangement = Arrangement.Center) {
        Row(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
            Column(
                modifier = Modifier.weight(1.1f).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Image(
                    painterResource(DesignSystemRes.drawable.app_logo),
                    "",
                    modifier = Modifier.size(120.dp).align(Alignment.CenterHorizontally),
                )
                Text(
                    "ARUNACHAL VIDYA NIDHI",
                    style = MaterialTheme.typography.headlineSmall,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    "शिक्षित अरुणाचल, विकसित अरुणाचल",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.W500
                )
            }

            Column(
                modifier = Modifier.weight(1.9f).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(fraction = 0.65f),
                    verticalArrangement = Arrangement.spacedBy(
                        space = 16.dp,
                        alignment = Alignment.CenterVertically
                    )
                ) {
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

        }
    }
}
