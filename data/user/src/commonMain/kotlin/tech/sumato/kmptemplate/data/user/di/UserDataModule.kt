package tech.sumato.kmptemplate.data.user.di

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import tech.sumato.kmptemplate.data.user.mapper.AuthMapper
import tech.sumato.kmptemplate.data.user.remote.AuthApi
import tech.sumato.kmptemplate.data.user.repository.AuthRepositoryImpl
import tech.sumato.kmptemplate.domain.user.repository.AuthRepository

val UserDataModule = module {
    singleOf(::AuthApi)
    singleOf(::AuthMapper)
    singleOf(::AuthRepositoryImpl) { bind<AuthRepository>() }
}
