package tech.sumato.avn.mp.feature.district_dashboard.presentation.effect

import tech.sumato.avn.mp.core.navigation.Route

sealed interface DistrictDashboardEffect {
    data class ShowSnackbar(val message: String) : DistrictDashboardEffect

    data class Navigate(val route: String) : DistrictDashboardEffect
}
