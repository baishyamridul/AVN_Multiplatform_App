package tech.sumato.avn.mp.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.cinterop.ExperimentalForeignApi
import okio.Path.Companion.toPath
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
private fun iosDataStorePath(): String {
    val dir = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return "${dir?.path ?: "."}/app_preferences.preferences_pb"
}

actual val DataStoreModule: Module = module {
    single<DataStore<Preferences>> {
        val path = iosDataStorePath()
        PreferenceDataStoreFactory.createWithPath { path.toPath() }
    }
    single { UserPreferencesStorage(get()) }
}
