package tech.sumato.avn.mp.data.districtDashboard.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import tech.sumato.avn.mp.core.network.model.SuccessResponseWrapper
import tech.sumato.avn.mp.data.districtDashboard.dto.DistrictDashboardDataDto

class DistrictDashboardApi(
    private val httpClient: HttpClient,
) {
    suspend fun getDistrictDashboard(districtId: Int): SuccessResponseWrapper<DistrictDashboardDataDto> {
        return httpClient.get("district-dashboard") {
            if (districtId != -1)
                parameter("districtId", districtId)
        }.body()
    }
}
