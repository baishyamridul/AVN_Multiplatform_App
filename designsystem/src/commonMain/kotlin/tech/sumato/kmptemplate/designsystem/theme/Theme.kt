package tech.sumato.kmptemplate.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = MainColor,
    onPrimary = SecondaryColor,
    primaryContainer = Color(0xFFD6E0E3),
    onPrimaryContainer = MainColor,
    secondary = AccentColor,
    onSecondary = SecondaryColor,
    secondaryContainer = Color(0xFFE1EAEC),
    onSecondaryContainer = AccentColor,
    tertiary = Color(0xFF456073),
    onTertiary = Color.White,
    background = SecondaryColor,
    onBackground = MainColor,
    surface = Color.White,
    onSurface = MainColor,
    surfaceVariant = Color(0xFFE8EEF0),
    onSurfaceVariant = Color(0xFF42484D),
    outline = MainColor.copy(alpha = 0.2f),
    outlineVariant = Color(0xFFC4CCCF),
    error = Color(0xFFBA1A1A),
    onError = Color.White,
)

private val DarkColorScheme = darkColorScheme(
    primary = SecondaryColor,
    onPrimary = MainColor,
    primaryContainer = MainColor.copy(alpha = 0.8f),
    onPrimaryContainer = SecondaryColor,
    secondary = Color(0xFFC4D7DC),
    onSecondary = Color(0xFF1C2F34),
    secondaryContainer = AccentColor,
    onSecondaryContainer = Color(0xFFD6E0E3),
    tertiary = Color(0xFF8DB0C3),
    onTertiary = Color(0xFF1C3B4A),
    background = AccentColor,
    onBackground = SecondaryColor,
    surface = MainColor,
    onSurface = SecondaryColor,
    surfaceVariant = Color(0xFF3A464A),
    onSurfaceVariant = Color(0xFFC4CCCF),
    outline = SecondaryColor.copy(alpha = 0.2f),
    outlineVariant = Color(0xFF42484D),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
)

@Composable
fun KMPTemplateTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val typography = appTypography()

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content,
    )
}
