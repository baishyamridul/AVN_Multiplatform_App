package tech.sumato.avn.mp.data.school.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import tech.sumato.avn.mp.core.network.model.SuccessResponseWrapper
import tech.sumato.avn.mp.data.school.dto.SchoolDto

class SchoolApi(
    private val httpClient: HttpClient,
) {

    suspend fun getSchools(districtId: Int): SuccessResponseWrapper<List<SchoolDto>> {
        return httpClient.get("district-schools") {
            if (districtId != -1)
                parameter("districtId", districtId)
        }.body()
    }

}



