package tech.sumato.avn.mp.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import org.koin.core.module.Module
import org.koin.dsl.module
import java.io.File

actual val DataStoreModule: Module = module {
    single<DataStore<Preferences>> {
        val context = get<Context>()
        PreferenceDataStoreFactory.create {
            File(context.filesDir, "app_preferences.preferences_pb")
        }
    }
    single { UserPreferencesStorage(get()) }
}
