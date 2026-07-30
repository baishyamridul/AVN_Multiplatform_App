package tech.sumato.avn.mp.data.districtDashboard.di

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import tech.sumato.avn.mp.data.districtDashboard.api.DistrictDashboardApi
import tech.sumato.avn.mp.data.districtDashboard.mapper.DistrictDashboardMapper
import tech.sumato.avn.mp.data.districtDashboard.repository.DistrictDashboardRepositoryImpl
import tech.sumato.avn.mp.domain.districtDashboard.repository.DistrictDashboardRepository

val DistrictDashboardDataModule = module {
    singleOf(::DistrictDashboardApi)
    singleOf(::DistrictDashboardMapper)
    singleOf(::DistrictDashboardRepositoryImpl) { bind<DistrictDashboardRepository>() }
}
