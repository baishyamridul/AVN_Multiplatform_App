package tech.sumato.avn.mp.domain.school.usecase

import tech.sumato.avn.mp.domain.school.model.SchoolDetailsModel
import tech.sumato.avn.mp.domain.school.repository.SchoolRepository

class GetSchoolDetailsUseCase(
    private val repository: SchoolRepository,
) {

    suspend operator fun invoke(schoolId: String): SchoolDetailsModel =
        repository.getSchoolDetails(schoolId)

}