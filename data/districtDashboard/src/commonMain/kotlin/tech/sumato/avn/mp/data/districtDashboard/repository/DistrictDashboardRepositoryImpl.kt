package tech.sumato.avn.mp.data.districtDashboard.repository

import tech.sumato.avn.mp.data.districtDashboard.api.DistrictDashboardApi
import tech.sumato.avn.mp.data.districtDashboard.mapper.DistrictDashboardMapper
import tech.sumato.avn.mp.domain.districtDashboard.model.DistrictDashboardData
import tech.sumato.avn.mp.domain.districtDashboard.repository.DistrictDashboardRepository

class DistrictDashboardRepositoryImpl(
    private val api: DistrictDashboardApi,
    private val mapper: DistrictDashboardMapper,
) : DistrictDashboardRepository {

    override suspend fun getDistrictDashboardData(): DistrictDashboardData {
        val response = api.getDistrictDashboard()
        return mapper.toDomain(response.data)
    }
}
