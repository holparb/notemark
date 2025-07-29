package com.holparb.notemark.core.domain.user_preferences

interface UserPreferences {

    suspend fun saveUsername(username: String)
    suspend fun getUsername(): String
}