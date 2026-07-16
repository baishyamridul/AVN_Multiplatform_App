package tech.sumato.kmptemplate.domain.dashboard.repository

import tech.sumato.kmptemplate.domain.dashboard.model.DashboardData

interface DashboardRepository {
    suspend fun getDashboardData(): DashboardData
}
