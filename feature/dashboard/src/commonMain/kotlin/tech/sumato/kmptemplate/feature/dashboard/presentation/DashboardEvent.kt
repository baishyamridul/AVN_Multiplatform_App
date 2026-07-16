package tech.sumato.kmptemplate.feature.dashboard.presentation

import tech.sumato.kmptemplate.domain.dashboard.model.DashboardData

sealed interface DashboardEvent {
    data object LoadDashboard : DashboardEvent
    data object Retry : DashboardEvent
}
