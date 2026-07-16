package tech.sumato.kmptemplate.feature.dashboard.presentation

import tech.sumato.kmptemplate.domain.dashboard.model.DashboardData

sealed interface DashboardState {
    data object Loading : DashboardState
    data class Success(val data: DashboardData) : DashboardState
    data class Error(val message: String) : DashboardState
}
