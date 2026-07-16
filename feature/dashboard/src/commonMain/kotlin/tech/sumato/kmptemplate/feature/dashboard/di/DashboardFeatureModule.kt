package tech.sumato.kmptemplate.feature.dashboard.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import tech.sumato.kmptemplate.domain.dashboard.usecase.GetDashboardDataUseCase
import tech.sumato.kmptemplate.feature.dashboard.presentation.DashboardViewModel

val DashboardFeatureModule = module {
    factoryOf(::GetDashboardDataUseCase)
    factoryOf(::DashboardViewModel)
}
