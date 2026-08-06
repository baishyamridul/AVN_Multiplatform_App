package tech.sumato.avn.mp.domain.school.repository

import tech.sumato.avn.mp.domain.school.model.SchoolModel

interface SchoolRepository {

    suspend fun getSchools(districtId: Int): List<SchoolModel>

}
