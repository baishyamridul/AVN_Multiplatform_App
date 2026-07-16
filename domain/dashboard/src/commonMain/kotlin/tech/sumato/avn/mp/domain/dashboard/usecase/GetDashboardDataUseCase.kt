package tech.sumato.avn.mp.domain.dashboard.usecase

import tech.sumato.avn.mp.domain.dashboard.model.DashboardData
import tech.sumato.avn.mp.domain.dashboard.repository.DashboardRepository

class GetDashboardDataUseCase(
    private val repository: DashboardRepository,
) {
    suspend operator fun invoke(): DashboardData {
        return repository.getDashboardData()
    }
}
