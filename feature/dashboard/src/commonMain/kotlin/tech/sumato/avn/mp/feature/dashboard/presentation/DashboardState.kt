package tech.sumato.avn.mp.feature.dashboard.presentation

import tech.sumato.avn.mp.domain.dashboard.model.DashboardData

sealed interface DashboardState {
    data object Loading : DashboardState
    data class Success(val data: DashboardData) : DashboardState
    data class Error(val message: String) : DashboardState
}
