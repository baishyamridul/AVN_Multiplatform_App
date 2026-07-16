package tech.sumato.avn.mp.data.dashboard.di

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import tech.sumato.avn.mp.data.dashboard.repository.DashboardRepositoryImpl
import tech.sumato.avn.mp.domain.dashboard.repository.DashboardRepository

val DashboardDataModule = module {
    singleOf(::DashboardRepositoryImpl) { bind<DashboardRepository>() }
}
