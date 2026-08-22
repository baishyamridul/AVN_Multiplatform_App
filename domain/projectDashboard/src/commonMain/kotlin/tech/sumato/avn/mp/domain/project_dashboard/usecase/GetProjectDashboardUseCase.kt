package tech.sumato.avn.mp.domain.project_dashboard.usecase

import tech.sumato.avn.mp.domain.project_dashboard.model.ProjectDashboard
import tech.sumato.avn.mp.domain.project_dashboard.repository.ProjectDashboardRepository

class GetProjectDashboardUseCase(
    private val repository: ProjectDashboardRepository,
) {
    suspend operator fun invoke(): ProjectDashboard = repository.get()
}
