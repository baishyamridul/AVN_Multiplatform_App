package tech.sumato.avn.mp.data.districtDashboard.repository

import tech.sumato.avn.mp.data.districtDashboard.api.DistrictDashboardApi
import tech.sumato.avn.mp.data.districtDashboard.mapper.DistrictDashboardMapper
import tech.sumato.avn.mp.domain.districtDashboard.model.DistrictDashboardData
import tech.sumato.avn.mp.domain.districtDashboard.repository.DistrictDashboardRepository

class DistrictDashboardRepositoryImpl(
    private val api: DistrictDashboardApi,
    private val mapper: DistrictDashboardMapper,
) : DistrictDashboardRepository {

    override suspend fun getDistrictDashboardData(districtId: Int): DistrictDashboardData {
        val response = api.getDistrictDashboard(districtId)
        val data = response.data ?: throw IllegalStateException("Dashboard data is missing")
        return mapper.toDomain(data)
    }
}
