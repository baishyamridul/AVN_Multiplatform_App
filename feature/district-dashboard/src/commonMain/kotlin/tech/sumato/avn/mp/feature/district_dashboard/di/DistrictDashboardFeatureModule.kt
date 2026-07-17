package tech.sumato.avn.mp.feature.district_dashboard.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import tech.sumato.avn.mp.feature.district_dashboard.presentation.DistrictDashboardViewModel

val DistrictDashboardFeatureModule = module {
    viewModelOf(::DistrictDashboardViewModel)
}
