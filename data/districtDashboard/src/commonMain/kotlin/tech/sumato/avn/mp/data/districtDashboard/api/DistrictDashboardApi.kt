package tech.sumato.avn.mp.data.districtDashboard.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import tech.sumato.avn.mp.data.districtDashboard.dto.DistrictDashboardResponseDto

class DistrictDashboardApi(
    private val httpClient: HttpClient,
) {
    suspend fun getDistrictDashboard(districtId: Int): DistrictDashboardResponseDto {
        return httpClient.get("district-dashboard") {
            if (districtId != -1)
                parameter("districtId", districtId)
        }.body()
    }
}
