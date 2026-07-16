package tech.sumato.avn.mp.domain.dashboard.repository

import tech.sumato.avn.mp.domain.dashboard.model.DashboardData

interface DashboardRepository {
    suspend fun getDashboardData(): DashboardData
}
