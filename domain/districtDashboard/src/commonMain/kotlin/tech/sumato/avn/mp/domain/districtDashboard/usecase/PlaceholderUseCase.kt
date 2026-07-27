package tech.sumato.avn.mp.domain.districtDashboard.usecase

import tech.sumato.avn.mp.domain.districtDashboard.model.PlaceholderModel
import tech.sumato.avn.mp.domain.districtDashboard.repository.PlaceholderRepository

class PlaceholderUseCase(
    private val repository: PlaceholderRepository,
) {
    suspend operator fun invoke(): List<PlaceholderModel> = repository.getData()
}
