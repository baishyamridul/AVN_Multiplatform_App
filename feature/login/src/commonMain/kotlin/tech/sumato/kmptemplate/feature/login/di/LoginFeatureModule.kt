package tech.sumato.kmptemplate.feature.login.di

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import tech.sumato.kmptemplate.domain.user.usecase.LoginUseCase
import tech.sumato.kmptemplate.feature.login.presentation.LoginViewModel

val LoginFeatureModule = module {
    factoryOf(::LoginUseCase)
    viewModelOf(::LoginViewModel)
}
