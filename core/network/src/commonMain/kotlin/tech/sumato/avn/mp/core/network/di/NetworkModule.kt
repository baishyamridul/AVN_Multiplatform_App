package tech.sumato.avn.mp.core.network.di

import org.koin.dsl.module
import tech.sumato.avn.mp.core.network.HttpClientFactory
import tech.sumato.avn.mp.core.network.NetworkConfig

val NetworkModule = module {
    single { NetworkConfig(baseUrl = "https://isam.sumato.tech/api/v1/") }
    single { HttpClientFactory.create(get()) }
}
