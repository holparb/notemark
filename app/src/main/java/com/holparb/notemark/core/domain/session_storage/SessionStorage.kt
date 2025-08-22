package com.holparb.notemark.core.domain.session_storage

import kotlinx.coroutines.flow.Flow

interface SessionStorage {

    suspend fun updateSessionData(sessionData: SessionData)
    suspend fun getSessionData(): SessionData
    fun observeSessionData(): Flow<SessionData>
}