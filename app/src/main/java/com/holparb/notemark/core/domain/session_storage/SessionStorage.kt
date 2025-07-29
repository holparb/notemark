package com.holparb.notemark.core.domain.session_storage

interface SessionStorage {

    suspend fun updateSessionData(sessionData: SessionData)
    suspend fun getSessionData(): SessionData
}