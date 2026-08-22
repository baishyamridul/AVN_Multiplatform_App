package tech.sumato.avn.mp.feature.project_dashboard.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import tech.sumato.avn.mp.feature.project_dashboard.presentation.ProjectDashboardViewModel

val ProjectDashboardFeatureModule = module {
    viewModelOf(::ProjectDashboardViewModel)
}
