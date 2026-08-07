package tech.sumato.avn.mp.domain.school.repository

import tech.sumato.avn.mp.domain.school.model.SchoolDetailsModel
import tech.sumato.avn.mp.domain.school.model.SchoolModel

interface SchoolRepository {

    suspend fun getSchools(districtId: Int): List<SchoolModel>

    suspend fun getSchoolDetails(schoolId: String): SchoolDetailsModel

}
