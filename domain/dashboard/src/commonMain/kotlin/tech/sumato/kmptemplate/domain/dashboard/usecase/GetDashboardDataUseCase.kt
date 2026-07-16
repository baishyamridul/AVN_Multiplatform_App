package tech.sumato.kmptemplate.domain.dashboard.usecase

import tech.sumato.kmptemplate.domain.dashboard.model.DashboardData
import tech.sumato.kmptemplate.domain.dashboard.repository.DashboardRepository

class GetDashboardDataUseCase(
    private val repository: DashboardRepository,
) {
    suspend operator fun invoke(): DashboardData {
        return repository.getDashboardData()
    }
}
