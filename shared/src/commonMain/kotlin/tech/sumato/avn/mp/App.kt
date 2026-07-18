package tech.sumato.avn.mp

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.compose.KoinApplication
import org.koin.core.KoinApplication
import org.koin.dsl.koinConfiguration
import tech.sumato.avn.mp.core.navigation.Route
import tech.sumato.avn.mp.core.network.di.NetworkModule
import tech.sumato.avn.mp.data.dashboard.di.DashboardDataModule
import tech.sumato.avn.mp.data.user.di.UserDataModule
import tech.sumato.avn.mp.designsystem.theme.AVNTheme
import tech.sumato.avn.mp.feature.dashboard.di.DashboardFeatureModule
import tech.sumato.avn.mp.feature.dashboard.presentation.DashboardRoute
import tech.sumato.avn.mp.feature.district_dashboard.di.DistrictDashboardFeatureModule
import tech.sumato.avn.mp.feature.district_dashboard.presentation.DistrictDashboardRoute
import tech.sumato.avn.mp.feature.login.di.LoginFeatureModule
import tech.sumato.avn.mp.feature.login.presentation.LoginRoute
import tech.sumato.avn.mp.feature.map_analytics.di.MapAnalyticsFeatureModule
import tech.sumato.avn.mp.feature.map_analytics.presentation.MapAnalyticsRoute

@Composable
fun App() {
    KoinApplication(configuration = koinConfiguration(declaration = {
        modules(
            NetworkModule,
            DashboardDataModule,
            DashboardFeatureModule,
            UserDataModule,
            LoginFeatureModule,
            MapAnalyticsFeatureModule,
            DistrictDashboardFeatureModule,
        )
    }), content = {
        AVNTheme {
            val navController = rememberNavController()
            AppNavGraph(navController = navController)
        }
    })
}

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Route.DISTRICT_DASHBOARD,
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
            MapAnalyticsRoute()
        }

        composable(Route.DISTRICT_DASHBOARD) {
            DistrictDashboardRoute()
        }

    }
}
