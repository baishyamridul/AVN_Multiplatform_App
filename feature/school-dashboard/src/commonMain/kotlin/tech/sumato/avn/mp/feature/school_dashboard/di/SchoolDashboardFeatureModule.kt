package tech.sumato.avn.mp.feature.school_dashboard.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import tech.sumato.avn.mp.feature.school_dashboard.presentation.SchoolDashboardViewModel

val SchoolDashboardFeatureModule = module {
    viewModelOf(::SchoolDashboardViewModel)
}
