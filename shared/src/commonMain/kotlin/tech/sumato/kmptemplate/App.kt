package tech.sumato.kmptemplate

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.compose.KoinApplication
import tech.sumato.kmptemplate.core.navigation.Route
import tech.sumato.kmptemplate.core.network.di.NetworkModule
import tech.sumato.kmptemplate.data.dashboard.di.DashboardDataModule
import tech.sumato.kmptemplate.data.user.di.UserDataModule
import tech.sumato.kmptemplate.designsystem.theme.KMPTemplateTheme
import tech.sumato.kmptemplate.feature.dashboard.di.DashboardFeatureModule
import tech.sumato.kmptemplate.feature.dashboard.presentation.DashboardRoute
import tech.sumato.kmptemplate.feature.login.di.LoginFeatureModule
import tech.sumato.kmptemplate.feature.login.presentation.LoginRoute

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
        KMPTemplateTheme {
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
