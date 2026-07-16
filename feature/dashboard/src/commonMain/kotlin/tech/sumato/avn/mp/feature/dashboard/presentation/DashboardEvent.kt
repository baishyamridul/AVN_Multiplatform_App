package tech.sumato.avn.mp.feature.dashboard.presentation

import tech.sumato.avn.mp.domain.dashboard.model.DashboardData

sealed interface DashboardEvent {
    data object LoadDashboard : DashboardEvent
    data object Retry : DashboardEvent
}
