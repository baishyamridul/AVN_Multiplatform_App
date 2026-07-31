package tech.sumato.avn.mp.feature.login.di

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import tech.sumato.avn.mp.domain.user.usecase.GetUserDetailsUseCase
import tech.sumato.avn.mp.domain.user.usecase.LoginUseCase
import tech.sumato.avn.mp.feature.login.presentation.LoginViewModel

val LoginFeatureModule = module {
    factoryOf(::LoginUseCase)
    factoryOf(::GetUserDetailsUseCase)
    viewModelOf(::LoginViewModel)
}
