package tech.sumato.avn.mp.data.project_dashboard.mapper

import tech.sumato.avn.mp.data.project_dashboard.remote.ProjectDashboardDto
import tech.sumato.avn.mp.domain.project_dashboard.model.ProjectDashboard

class ProjectDashboardMapper {
    fun toDomain(dto: ProjectDashboardDto): ProjectDashboard {
        return ProjectDashboard(
            id = dto.id,
        )
    }
}
