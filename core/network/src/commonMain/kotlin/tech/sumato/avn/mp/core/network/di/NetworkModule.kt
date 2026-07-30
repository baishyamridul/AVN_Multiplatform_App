package tech.sumato.avn.mp.core.network.di

import kotlinx.serialization.json.Json
import org.koin.dsl.module
import tech.sumato.avn.mp.core.datastore.UserPreferencesStorage
import tech.sumato.avn.mp.core.network.HttpClientFactory
import tech.sumato.avn.mp.core.network.NetworkConfig

val NetworkModule = module {
    single { NetworkConfig(baseUrl = "https://isam.sumato.tech/api/v1/") }
    single<Json> {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            prettyPrint = false
            encodeDefaults = true
        }
    }
    single {
        val storage: UserPreferencesStorage = get()
        HttpClientFactory.create(
            config = get(),
            json = get(),
            tokenProvider = { storage.getAuth()?.accessToken }
        )
    }

}
