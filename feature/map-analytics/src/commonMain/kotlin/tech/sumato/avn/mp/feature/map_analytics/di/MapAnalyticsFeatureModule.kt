package tech.sumato.avn.mp.feature.map_analytics.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import tech.sumato.avn.mp.feature.map_analytics.presentation.MapAnalyticsViewModel

val MapAnalyticsFeatureModule = module {
    viewModelOf(::MapAnalyticsViewModel)
}
