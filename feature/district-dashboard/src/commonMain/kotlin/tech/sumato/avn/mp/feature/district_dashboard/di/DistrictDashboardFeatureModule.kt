package tech.sumato.avn.mp.feature.district_dashboard.di

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import tech.sumato.avn.mp.domain.districtDashboard.usecase.GetDistrictDashboardDataUseCase
import tech.sumato.avn.mp.domain.user.usecase.GetStoredUserDetailsUseCase
import tech.sumato.avn.mp.feature.district_dashboard.presentation.DistrictDashboardViewModel

val DistrictDashboardFeatureModule = module {
    factoryOf(::GetDistrictDashboardDataUseCase)
    factoryOf(::GetStoredUserDetailsUseCase)
    viewModelOf(::DistrictDashboardViewModel)
}
