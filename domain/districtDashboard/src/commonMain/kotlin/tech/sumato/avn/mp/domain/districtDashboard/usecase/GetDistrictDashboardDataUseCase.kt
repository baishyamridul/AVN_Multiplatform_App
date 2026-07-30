package tech.sumato.avn.mp.domain.districtDashboard.usecase

import tech.sumato.avn.mp.domain.districtDashboard.model.DistrictDashboardData
import tech.sumato.avn.mp.domain.districtDashboard.repository.DistrictDashboardRepository

class GetDistrictDashboardDataUseCase(
    private val repository: DistrictDashboardRepository,
) {
    suspend operator fun invoke(): DistrictDashboardData {
        return repository.getDistrictDashboardData()
    }
}
