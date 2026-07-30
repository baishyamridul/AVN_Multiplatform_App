package tech.sumato.avn.mp.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import tech.sumato.avn.mp.domain.user.model.AuthResult
import tech.sumato.avn.mp.domain.user.model.User

class UserPreferencesStorage(private val dataStore: DataStore<Preferences>) {

    private companion object {
        val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")
        val KEY_TOKEN_TYPE = stringPreferencesKey("token_type")
        val KEY_USER_ID = stringPreferencesKey("user_id")
        val KEY_USER_NAME = stringPreferencesKey("user_name")
        val KEY_USER_EMAIL = stringPreferencesKey("user_email")
        val KEY_USER_ROLE = stringPreferencesKey("user_role")
        val KEY_USER_PHONE = stringPreferencesKey("user_phone")
        val KEY_USER_PHOTO = stringPreferencesKey("user_photo")
        val KEY_USER_DESIGNATION = stringPreferencesKey("user_designation")
    }

    suspend fun saveAuth(result: AuthResult) {
        dataStore.edit { prefs ->
            prefs[KEY_ACCESS_TOKEN] = result.accessToken
            prefs[KEY_TOKEN_TYPE] = result.tokenType
            prefs[KEY_USER_ID] = result.user.id
            prefs[KEY_USER_NAME] = result.user.name
            prefs[KEY_USER_EMAIL] = result.user.email
            result.user.role?.let { prefs[KEY_USER_ROLE] = it }
            result.user.phone?.let { prefs[KEY_USER_PHONE] = it }
            result.user.photo?.let { prefs[KEY_USER_PHOTO] = it }
            result.user.designation?.let { prefs[KEY_USER_DESIGNATION] = it }
        }
    }

    data class AuthData(
        val accessToken: String,
        val tokenType: String,
        val user: User,
    )

    private fun readAuth(prefs: Preferences): AuthData? {
        val token = prefs[KEY_ACCESS_TOKEN] ?: return null
        val type = prefs[KEY_TOKEN_TYPE] ?: return null
        val id = prefs[KEY_USER_ID] ?: return null
        val name = prefs[KEY_USER_NAME] ?: return null
        val email = prefs[KEY_USER_EMAIL] ?: return null
        return AuthData(
            accessToken = token,
            tokenType = type,
            user = User(
                id = id,
                name = name,
                email = email,
                role = prefs[KEY_USER_ROLE],
                phone = prefs[KEY_USER_PHONE],
                photo = prefs[KEY_USER_PHOTO],
                designation = prefs[KEY_USER_DESIGNATION],
            ),
        )
    }

    fun observeAuth(): Flow<AuthData?> {
        return dataStore.data.map { readAuth(it) }
    }

    suspend fun getAuth(): AuthData? {
        return readAuth(dataStore.data.first())
    }

    suspend fun clear() {
        dataStore.edit { it.clear() }
    }
}
