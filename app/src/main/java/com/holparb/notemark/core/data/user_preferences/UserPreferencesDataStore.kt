package com.holparb.notemark.core.data.user_preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.holparb.notemark.core.domain.user_preferences.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class UserPreferencesDataStore(
    private val context: Context
): UserPreferences {

    companion object {
        private  val Context.userPreferencesDatastore by preferencesDataStore(
            name = "user_preferences"
        )
    }

    private val usernameKey = stringPreferencesKey("username")

    override suspend fun saveUsername(username: String) {
        context.userPreferencesDatastore.edit { prefs ->
            prefs[usernameKey] = username
        }
    }

    override suspend fun getUsername(): String {
        return context.userPreferencesDatastore.data.map { prefs ->
            prefs[usernameKey] ?: ""
        }.first()
    }
}