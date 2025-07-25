package com.holparb.notemark.core.data.user_preferences

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow

class UserPreferencesRepository(
    private val userPreferencesDataStore: DataStore<UserPreferences>
) {
    suspend fun updateUserData(userPreferences: UserPreferences) {
       userPreferencesDataStore.updateData {
           userPreferences
       }
    }

    suspend fun updateAccessToken(accessToken: String) {
        userPreferencesDataStore.updateData {
            it.copy(accessToken = accessToken)
        }
    }

    suspend fun updateRefreshToken(refreshToken: String) {
        userPreferencesDataStore.updateData {
            it.copy(refreshToken = refreshToken)
        }
    }

    fun getUserData(): Flow<UserPreferences> {
        return userPreferencesDataStore.data
    }
}