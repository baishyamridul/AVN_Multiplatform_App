package tech.sumato.avn.mp.domain.districtDashboard.repository

import tech.sumato.avn.mp.domain.districtDashboard.model.DistrictDashboardData

interface DistrictDashboardRepository {
    suspend fun getDistrictDashboardData(districtId: Int): DistrictDashboardData
}
