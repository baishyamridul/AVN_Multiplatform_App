package tech.sumato.avn.mp.core.network.di

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import tech.sumato.avn.mp.core.datastore.UserPreferencesStorage
import tech.sumato.avn.mp.core.network.HttpClientFactory
import tech.sumato.avn.mp.core.network.NetworkConfig

private val _logoutEvent = MutableSharedFlow<String>(extraBufferCapacity = 1)
val logoutEvent: SharedFlow<String> = _logoutEvent.asSharedFlow()

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
    single<SharedFlow<String>> { logoutEvent }
    single {
        val storage: UserPreferencesStorage = get()
        HttpClientFactory.create(
            config = get(),
            json = get(),
            tokenProvider = { storage.getAuth()?.accessToken },
            onUnauthorized = {
                storage.clear()
                _logoutEvent.emit("Session expired. Please login again.")
            },
        )
    }
}
