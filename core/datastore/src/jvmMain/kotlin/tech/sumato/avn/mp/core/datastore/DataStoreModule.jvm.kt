package tech.sumato.avn.mp.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import org.koin.core.module.Module
import org.koin.dsl.module
import java.io.File

actual val DataStoreModule: Module = module {
    single<DataStore<Preferences>> {
        val dir = File(System.getProperty("user.home"), ".avn_app")
        dir.mkdirs()
        PreferenceDataStoreFactory.create {
            File(dir, "app_preferences.preferences_pb")
        }
    }
    single { UserPreferencesStorage(get()) }
}
