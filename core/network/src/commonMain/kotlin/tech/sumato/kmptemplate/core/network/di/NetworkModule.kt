package tech.sumato.kmptemplate.core.network.di

import org.koin.dsl.module
import tech.sumato.kmptemplate.core.network.HttpClientFactory
import tech.sumato.kmptemplate.core.network.NetworkConfig

val NetworkModule = module {
    single { NetworkConfig(baseUrl = "https://api.example.com") }
    single { HttpClientFactory.create(get()) }
}
