package tech.sumato.avn.mp.domain.school.usecase

import tech.sumato.avn.mp.domain.school.model.SchoolModel
import tech.sumato.avn.mp.domain.school.repository.SchoolRepository

class GetSchoolsUseCase(
    private val repository: SchoolRepository,
) {

    suspend operator fun invoke(districtId: Int): List<SchoolModel> =
        repository.getSchools(districtId = districtId)
}
