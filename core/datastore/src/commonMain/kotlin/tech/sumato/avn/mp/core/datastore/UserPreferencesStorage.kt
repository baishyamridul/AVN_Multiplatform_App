package tech.sumato.avn.mp.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

data class StoredAuth(
    val accessToken: String,
    val tokenType: String,
    val userId: String,
    val name: String,
    val email: String,
    val role: String? = null,
    val phone: String? = null,
    val photo: String? = null,
    val designation: String? = null,
    val createdJson: String? = null,
    val districtsJson: String? = null,
)

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
        val KEY_USER_CREATED = stringPreferencesKey("user_created")
        val KEY_USER_DISTRICTS = stringPreferencesKey("user_districts")
    }

    suspend fun saveAuth(
        accessToken: String,
        tokenType: String,
        userId: String,
        name: String,
        email: String,
        role: String? = null,
        phone: String? = null,
        photo: String? = null,
        designation: String? = null,
    ) {
        dataStore.edit { prefs ->
            prefs[KEY_ACCESS_TOKEN] = accessToken
            prefs[KEY_TOKEN_TYPE] = tokenType
            prefs[KEY_USER_ID] = userId
            prefs[KEY_USER_NAME] = name
            prefs[KEY_USER_EMAIL] = email
            role?.let { prefs[KEY_USER_ROLE] = it }
            phone?.let { prefs[KEY_USER_PHONE] = it }
            photo?.let { prefs[KEY_USER_PHOTO] = it }
            designation?.let { prefs[KEY_USER_DESIGNATION] = it }
        }
    }

    suspend fun saveUser(
        userId: String,
        name: String,
        email: String,
        role: String? = null,
        phone: String? = null,
        photo: String? = null,
        designation: String? = null,
        createdJson: String? = null,
        districtsJson: String? = null,
    ) {
        dataStore.edit { prefs ->
            prefs[KEY_USER_ID] = userId
            prefs[KEY_USER_NAME] = name
            prefs[KEY_USER_EMAIL] = email
            role?.let { prefs[KEY_USER_ROLE] = it }
            phone?.let { prefs[KEY_USER_PHONE] = it }
            photo?.let { prefs[KEY_USER_PHOTO] = it }
            designation?.let { prefs[KEY_USER_DESIGNATION] = it }
            createdJson?.let { prefs[KEY_USER_CREATED] = it }
            districtsJson?.let { prefs[KEY_USER_DISTRICTS] = it }
        }
    }

    suspend fun getAccessToken(): String? {
        return dataStore.data.first()[KEY_ACCESS_TOKEN]
    }

    private fun readAuth(prefs: Preferences): StoredAuth? {
        val token = prefs[KEY_ACCESS_TOKEN] ?: return null
        val type = prefs[KEY_TOKEN_TYPE] ?: return null
        val id = prefs[KEY_USER_ID] ?: return null
        val name = prefs[KEY_USER_NAME] ?: return null
        val email = prefs[KEY_USER_EMAIL] ?: return null
        return StoredAuth(
            accessToken = token,
            tokenType = type,
            userId = id,
            name = name,
            email = email,
            role = prefs[KEY_USER_ROLE],
            phone = prefs[KEY_USER_PHONE],
            photo = prefs[KEY_USER_PHOTO],
            designation = prefs[KEY_USER_DESIGNATION],
            createdJson = prefs[KEY_USER_CREATED],
            districtsJson = prefs[KEY_USER_DISTRICTS],
        )
    }

    fun observeAuth(): Flow<StoredAuth?> {
        return dataStore.data.map { readAuth(it) }
    }

    suspend fun getAuth(): StoredAuth? {
        return readAuth(dataStore.data.first())
    }

    suspend fun clear() {
        dataStore.edit { it.clear() }
    }
}
