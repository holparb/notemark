package com.holparb.notemark.core.data.user_preferences

import androidx.datastore.core.DataStore
import com.holparb.notemark.auth.domain.token.Token
import kotlinx.coroutines.flow.first

class UserPreferencesDataStore(
    private val userPreferencesDataStore: DataStore<UserPreferences>
) {
    suspend fun updateUserData(userPreferences: UserPreferences) {
       userPreferencesDataStore.updateData {
           userPreferences
       }
    }

    suspend fun updateTokens(
        accessToken: String,
        refreshToken: String
    ) {
        userPreferencesDataStore.updateData {
            it.copy(
                accessToken = accessToken,
                refreshToken = refreshToken
            )
        }
    }

    suspend fun getTokens(): Token {
        val userPreferences = userPreferencesDataStore.data.first()
        return Token(
            accessToken = userPreferences.accessToken ?: "",
            refreshToken = userPreferences.refreshToken ?: ""
        )
    }

    suspend fun getUsername(): String {
        return userPreferencesDataStore.data.first().username!!
    }

}