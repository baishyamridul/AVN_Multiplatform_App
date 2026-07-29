package tech.sumato.avn.mp.data.districtDashboard.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import tech.sumato.avn.mp.data.districtDashboard.dto.DistrictDashboardResponseDto

class DistrictDashboardApi(
    private val httpClient: HttpClient,
) {
    suspend fun getDistrictDashboard(): DistrictDashboardResponseDto {
        return httpClient.get("district-dashboard") {
            headers.append("Authorization", "Bearer 132|f99hRNrJODQsoyQrtkxSeVJpU16RURjd3BnCh7bR614505fe")
        }.body()
    }
}
