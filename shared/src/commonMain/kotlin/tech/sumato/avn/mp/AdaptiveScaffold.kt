package tech.sumato.avn.mp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import tech.sumato.avn.mp.core.navigation.Route
import tech.sumato.avn.mp.designsystem.FormFactor
import tech.sumato.avn.mp.designsystem.LocalFormFactor

private val fullScreenRoutes = setOf(Route.LOGIN, Route.DISTRICT_DASHBOARD, Route.SCHOOL_DASHBOARD)

private data class NavDestination(
    val route: String,
    val label: String,
    val icon: ImageVector,
)

private val navDestinations = listOf(
    NavDestination(Route.DISTRICT_DASHBOARD, "District", Icons.Default.Home),
    NavDestination(Route.DASHBOARD, "Dashboard", Icons.Default.Dashboard),
    NavDestination(Route.MAP_ANALYTICS, "Map", Icons.Default.Map),
)

@Composable
fun AdaptiveScaffold(
    navController: NavHostController,
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
            val currentRoute =
                navController.currentBackStackEntryAsState().value?.destination?.route
            val showNav = currentRoute != null && currentRoute !in fullScreenRoutes

            when (formFactor) {
                FormFactor.Compact -> CompactScaffold(navController, showNav, content)
                FormFactor.Medium -> MediumScaffold(navController, showNav, content)
                FormFactor.Expanded -> ExpandedScaffold(navController, showNav, content)
            }
        }
    }
}

@Composable
private fun CompactScaffold(
    navController: NavHostController,
    showNav: Boolean,
    content: @Composable () -> Unit,
) {
    Scaffold(
        bottomBar = {
            AnimatedVisibility(visible = showNav) {
                CompactNavBar(navController)
            }
        },
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(padding),
        ) {
            content()
        }
    }
}

@Composable
private fun CompactNavBar(navController: NavHostController) {
    val currentRoute by navController.currentBackStackEntryAsState()

    NavigationBar {
        navDestinations.forEach { dest ->
            val selected = currentRoute?.destination?.route == dest.route
            NavigationBarItem(
                selected = selected,
                onClick = { navigateTo(navController, dest.route) },
                icon = { Icon(dest.icon, dest.label) },
                label = { Text(dest.label) },
            )
        }
    }
}

@Composable
private fun MediumScaffold(
    navController: NavHostController,
    showNav: Boolean,
    content: @Composable () -> Unit,
) {
    Scaffold { paddingValues ->
        Row(modifier = Modifier.fillMaxSize().padding(paddingValues = paddingValues)) {
            if (showNav) {
                MediumNavRail(navController)
            }
            content()
        }
    }
}

@Composable
private fun MediumNavRail(navController: NavHostController) {
    val currentRoute by navController.currentBackStackEntryAsState()

    NavigationRail {
        Spacer(Modifier.height(8.dp))
        navDestinations.forEach { dest ->
            val selected = currentRoute?.destination?.route == dest.route
            NavigationRailItem(
                selected = selected,
                onClick = { navigateTo(navController, dest.route) },
                icon = { Icon(dest.icon, dest.label) },
                label = { Text(dest.label) },
            )
        }
    }
}

@Composable
private fun ExpandedScaffold(
    navController: NavHostController,
    showNav: Boolean,
    content: @Composable () -> Unit,
) {

    Scaffold { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            if (!showNav) {
                content()
                return@Scaffold
            }

            PermanentNavigationDrawer(
                drawerContent = {
                    ExpandedDrawerContent(navController)
                },
            ) {
                content()
            }
        }
    }


}

@Composable
private fun ExpandedDrawerContent(navController: NavHostController) {
    val currentRoute by navController.currentBackStackEntryAsState()

    Column(
        modifier = Modifier
            .width(280.dp)
            .fillMaxHeight()
            .padding(16.dp),
    ) {
        Text(
            "Navigation",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 16.dp),
        )

        navDestinations.forEach { dest ->
            val selected = currentRoute?.destination?.route == dest.route
            NavigationDrawerItem(
                selected = selected,
                onClick = { navigateTo(navController, dest.route) },
                icon = { Icon(dest.icon, dest.label) },
                label = { Text(dest.label) },
            )
        }
    }
}

private fun navigateTo(navController: NavHostController, route: String) {
    if (navController.currentDestination?.route != route) {
        navController.navigate(route) {
            popUpTo(Route.DISTRICT_DASHBOARD) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }
}
