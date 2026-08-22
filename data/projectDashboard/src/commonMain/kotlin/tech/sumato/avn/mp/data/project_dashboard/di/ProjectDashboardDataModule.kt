package tech.sumato.avn.mp.data.project_dashboard.di

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import tech.sumato.avn.mp.data.project_dashboard.mapper.ProjectDashboardMapper
import tech.sumato.avn.mp.data.project_dashboard.remote.ProjectDashboardApi
import tech.sumato.avn.mp.data.project_dashboard.repository.ProjectDashboardRepositoryImpl
import tech.sumato.avn.mp.domain.project_dashboard.repository.ProjectDashboardRepository

val ProjectDashboardDataModule = module {
    singleOf(::ProjectDashboardApi)
    singleOf(::ProjectDashboardMapper)
    singleOf(::ProjectDashboardRepositoryImpl) { bind<ProjectDashboardRepository>() }
}
