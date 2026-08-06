package tech.sumato.avn.mp

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.savedstate.read
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.emptyFlow
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import org.koin.core.KoinApplication
import org.koin.dsl.KoinConfiguration
import tech.sumato.avn.mp.core.datastore.DataStoreModule
import tech.sumato.avn.mp.core.navigation.Route
import tech.sumato.avn.mp.core.network.di.NetworkModule
import tech.sumato.avn.mp.data.dashboard.di.DashboardDataModule
import tech.sumato.avn.mp.data.districtDashboard.di.DistrictDashboardDataModule
import tech.sumato.avn.mp.data.school.di.SchoolDataModule
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
import tech.sumato.avn.mp.feature.school_dashboard.di.SchoolDashboardFeatureModule
import tech.sumato.avn.mp.feature.school_dashboard.presentation.SchoolDashboardRoute

@Composable
fun App(
    platformConfiguration: KoinApplication.() -> Unit = {},
) {
    KoinApplication(configuration = KoinConfiguration {
        platformConfiguration()
        modules(
            DataStoreModule,
            NetworkModule,
            DashboardDataModule,
            DashboardFeatureModule,
            DistrictDashboardDataModule,
            UserDataModule,
            LoginFeatureModule,
            MapAnalyticsFeatureModule,
            DistrictDashboardFeatureModule,
            SchoolDashboardFeatureModule,
            SchoolDataModule,
        )
    }, content = {
        AVNTheme {
            val navController = rememberNavController()
            AdaptiveScaffold(navController = navController) {
                AppNavGraph(navController = navController)
            }
        }
    })
}

@Composable
fun AppNavGraph(navController: NavHostController) {
    val logoutEvent: SharedFlow<String> = koinInject()
    var pendingLogoutMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        logoutEvent.collect { message ->
            pendingLogoutMessage = message
            navController.navigate(Route.LOGIN) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Route.LOGIN,
    ) {
        composable(Route.LOGIN) {
            val msg = pendingLogoutMessage
            if (msg != null) pendingLogoutMessage = null
            LoginRoute(
                onNavigateToDashboard = {
                    navController.navigate(Route.DISTRICT_DASHBOARD) {
                        popUpTo(Route.LOGIN) { inclusive = true }
                    }
                },
                sessionExpiredMessage = msg,
            )
        }
        composable(Route.DASHBOARD) {
            DashboardRoute()
        }

        composable(Route.MAP_ANALYTICS) {
            MapAnalyticsRoute()
        }

        composable(Route.DISTRICT_DASHBOARD) {
            DistrictDashboardRoute(
                onNavigation = { route ->
                    navController.navigate(route)
                }
            )
        }

        composable(
            route = Route.SCHOOL_DASHBOARD,
            arguments = listOf(
                navArgument("districtId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            val initialDistrictId = backStackEntry.arguments?.read { getIntOrNull("districtId") }
            SchoolDashboardRoute(
                initialDistrictId = initialDistrictId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

    }
}
