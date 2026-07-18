package tech.sumato.avn.mp

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import tech.sumato.avn.mp.core.navigation.Route
import tech.sumato.avn.mp.designsystem.FormFactor
import tech.sumato.avn.mp.designsystem.LocalFormFactor


@Composable
fun ScreenScaffold(
    navController: NavController,
    content: @Composable () -> Unit,
) {

    BoxWithConstraints {
        val widthDp = maxWidth
        val formFactor = when {
            widthDp < 600.dp -> FormFactor.Compact
            widthDp < 840.dp -> FormFactor.Medium
            else -> FormFactor.Expanded
        }

        CompositionLocalProvider(LocalFormFactor provides formFactor) {
            CompactScreenScaffold(navController, content)
        }

    }
}

@Composable
fun CompactScreenScaffold(
    navController: NavController,
    content: @Composable () -> Unit,
) {


}
