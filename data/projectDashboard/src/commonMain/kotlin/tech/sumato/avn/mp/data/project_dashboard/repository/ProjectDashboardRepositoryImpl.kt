package tech.sumato.avn.mp.data.project_dashboard.repository

import tech.sumato.avn.mp.data.project_dashboard.mapper.ProjectDashboardMapper
import tech.sumato.avn.mp.data.project_dashboard.remote.ProjectDashboardApi
import tech.sumato.avn.mp.domain.project_dashboard.model.ProjectDashboard
import tech.sumato.avn.mp.domain.project_dashboard.repository.ProjectDashboardRepository

class ProjectDashboardRepositoryImpl(
    private val api: ProjectDashboardApi,
    private val mapper: ProjectDashboardMapper,
) : ProjectDashboardRepository {

    override suspend fun get(): ProjectDashboard {
        TODO("implement")
    }
}
