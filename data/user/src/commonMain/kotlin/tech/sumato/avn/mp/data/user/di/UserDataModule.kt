package tech.sumato.avn.mp.data.user.di

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import tech.sumato.avn.mp.data.user.mapper.AuthMapper
import tech.sumato.avn.mp.data.user.mapper.MeMapper
import tech.sumato.avn.mp.data.user.remote.AuthApi
import tech.sumato.avn.mp.data.user.repository.AuthRepositoryImpl
import tech.sumato.avn.mp.domain.user.repository.AuthRepository

val UserDataModule = module {
    singleOf(::AuthApi)
    singleOf(::AuthMapper)
    singleOf(::MeMapper)
    singleOf(::AuthRepositoryImpl) { bind<AuthRepository>() }
}
