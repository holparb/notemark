package com.holparb.notemark.core.domain.user_preferences

import kotlinx.coroutines.flow.Flow

interface UserPreferences {

    suspend fun saveUsername(username: String)
    suspend fun getUsername(): String

    suspend fun saveUserId(userId: String)
    suspend fun getUserId(): String

    suspend fun saveLastSyncTimestamp(timestamp: Long)
    fun observeLastSyncTimestamp(): Flow<Long>

    suspend fun saveSyncInterval(syncIntervalMinutes: Int)
    fun observeSyncInterval(): Flow<Int>
}