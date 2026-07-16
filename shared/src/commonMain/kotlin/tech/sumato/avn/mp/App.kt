package tech.sumato.avn.mp

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.compose.KoinApplication
import tech.sumato.avn.mp.core.navigation.Route
import tech.sumato.avn.mp.core.network.di.NetworkModule
import tech.sumato.avn.mp.data.dashboard.di.DashboardDataModule
import tech.sumato.avn.mp.data.user.di.UserDataModule
import tech.sumato.avn.mp.designsystem.theme.AVNTheme
import tech.sumato.avn.mp.feature.dashboard.di.DashboardFeatureModule
import tech.sumato.avn.mp.feature.dashboard.presentation.DashboardRoute
import tech.sumato.avn.mp.feature.login.di.LoginFeatureModule
import tech.sumato.avn.mp.feature.login.presentation.LoginRoute

@Composable
fun App() {
    KoinApplication(application = {
        modules(
            NetworkModule,
            DashboardDataModule,
            DashboardFeatureModule,
            UserDataModule,
            LoginFeatureModule,
        )
    }) {
        AVNTheme {
            val navController = rememberNavController()
            AppNavGraph(navController = navController)
        }
    }
}

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Route.LOGIN,
    ) {
        composable(Route.LOGIN) {
            LoginRoute(
                onNavigateToDashboard = {
                    navController.navigate(Route.DASHBOARD) {
                        popUpTo(Route.LOGIN) { inclusive = true }
                    }
                },
            )
        }
        composable(Route.DASHBOARD) {
            DashboardRoute()
        }

        composable(Route.MAP_ANALYTICS) {
            //
        }

    }
}
