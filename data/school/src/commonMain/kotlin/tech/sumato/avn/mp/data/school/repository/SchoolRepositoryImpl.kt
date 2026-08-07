package tech.sumato.avn.mp.data.school.repository

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import tech.sumato.avn.mp.core.network.model.ErrorResponseWrapper
import tech.sumato.avn.mp.data.school.mapper.SchoolMapper
import tech.sumato.avn.mp.data.school.remote.SchoolApi
import tech.sumato.avn.mp.domain.common.model.exception.ResponseDataModel
import tech.sumato.avn.mp.domain.common.model.exception.ResponseExceptionModel
import tech.sumato.avn.mp.domain.school.model.SchoolDetailsModel
import tech.sumato.avn.mp.domain.school.model.SchoolModel
import tech.sumato.avn.mp.domain.school.repository.SchoolRepository

class SchoolRepositoryImpl(
    private val api: SchoolApi,
    private val mapper: SchoolMapper,
    private val json: Json,
) : SchoolRepository {

    override suspend fun getSchools(districtId: Int): List<SchoolModel> {

        val result = try {
            val response = api.getSchools(districtId = districtId)
            response.data
        } catch (e: ClientRequestException) {
            val errorBody = try {
                json.decodeFromString<ErrorResponseWrapper>(e.response.bodyAsText())
            } catch (_: Exception) {
                ErrorResponseWrapper(status = e.response.status.value, message = e.message)
            }
            throw ResponseExceptionModel(
                message = errorBody.message,
                fieldErrors = errorBody.errors,
            )
        }

        return result?.map { schoolDto -> mapper.toDomain(schoolDto) } ?: emptyList()

    }

    override suspend fun getSchoolDetails(schoolId: String): SchoolDetailsModel {

        val result = try {
            val response = api.getSchoolDetails(schoolId = schoolId)
            response.data
        } catch (e: ClientRequestException) {
            val errorBody = try {
                json.decodeFromString<ErrorResponseWrapper>(e.response.bodyAsText())
            } catch (_: Exception) {
                ErrorResponseWrapper(status = e.response.status.value, message = e.message)
            }
            throw ResponseExceptionModel(
                message = errorBody.message,
                fieldErrors = errorBody.errors,
            )
        }

        return result?.let { mapper.toDomain(it) } ?: error("school details empty")

    }

}
