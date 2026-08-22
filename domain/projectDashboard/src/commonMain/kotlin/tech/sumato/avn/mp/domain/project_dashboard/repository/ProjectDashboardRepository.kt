package tech.sumato.avn.mp.domain.project_dashboard.repository

import tech.sumato.avn.mp.domain.project_dashboard.model.ProjectDashboard

interface ProjectDashboardRepository {
    suspend fun get(): ProjectDashboard
}
