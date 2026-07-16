package tech.sumato.avn.mp.feature.dashboard.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import tech.sumato.avn.mp.domain.dashboard.usecase.GetDashboardDataUseCase
import tech.sumato.avn.mp.feature.dashboard.presentation.DashboardViewModel

val DashboardFeatureModule = module {
    factoryOf(::GetDashboardDataUseCase)
    factoryOf(::DashboardViewModel)
}
