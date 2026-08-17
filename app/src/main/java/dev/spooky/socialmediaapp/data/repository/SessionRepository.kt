package dev.spooky.socialmediaapp.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import dev.spooky.socialmediaapp.data.models.AuthData
import dev.spooky.socialmediaapp.data.models.UserInfo
import kotlinx.coroutines.flow.last
import kotlinx.serialization.json.Json

class SessionRepository(
    private val authKey: Preferences.Key<String>,
    private val userInfoKey: Preferences.Key<String>,
    private val preferences: DataStore<Preferences>
) {

    suspend fun setUserInfoPreferences(info: UserInfo) {
        preferences.edit { prefs ->
            prefs[userInfoKey] = Json.encodeToString(info)
        }
    }

    suspend fun setAuthPreferences(data: AuthData) {
        preferences.edit { prefs ->
            prefs[authKey] = Json.encodeToString(data)
        }
    }

    suspend fun getUserInfoPreferences(): UserInfo? {
        val pref = preferences.data.last()[userInfoKey] ?: return null
        return Json.decodeFromString(pref)
    }

    suspend fun getAuthDataPreferences(): AuthData? {
        val pref = preferences.data.last()[authKey] ?: return null
        return Json.decodeFromString(pref)
    }
}