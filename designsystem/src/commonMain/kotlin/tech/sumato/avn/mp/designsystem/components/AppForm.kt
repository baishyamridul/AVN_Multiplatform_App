package tech.sumato.avn.mp.designsystem.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    trailing: @Composable (() -> Unit)? = null,
    isSecured: Boolean = false,
    error: String? = null,
) {
    Column(modifier = modifier) {
//        Text(
//            label,
//            style = MaterialTheme.typography.labelMedium,
//            color = MaterialTheme.colorScheme.onSurface,
//        )
//        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            isError = error != null,
            placeholder = if (placeholder.isNotEmpty()) {
                {
                    Text(
                        placeholder,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                            alpha = 0.75f
                        ),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            } else null,
            textStyle = MaterialTheme.typography.bodyMedium,
            enabled = enabled,
            readOnly = readOnly,
            singleLine = singleLine,
            trailingIcon = trailing,
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                errorBorderColor = MaterialTheme.colorScheme.error,
                errorContainerColor = MaterialTheme.colorScheme.surface,
            ),
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (isSecured) PasswordVisualTransformation() else VisualTransformation.None,

            )
        if (error != null) {
            Spacer(Modifier.height(4.dp))
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}
