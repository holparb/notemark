package com.holparb.notemark.core.data.session_storage

import android.content.Context
import androidx.datastore.dataStore
import com.holparb.notemark.core.domain.session_storage.SessionData
import com.holparb.notemark.core.domain.session_storage.SessionStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class SessionStorageDataStore(
    private val context: Context
): SessionStorage {

    companion object {
        private val Context.sessionStorageDataStore by dataStore(
            fileName = "session_storage",
            serializer = SessionDataSerializer
        )
    }

    override suspend fun updateSessionData(sessionData: SessionData) {
        context.sessionStorageDataStore.updateData { sessionData }
    }

    override suspend fun getSessionData(): SessionData {
        return context.sessionStorageDataStore.data.first()
    }

    override fun observeSessionData(): Flow<SessionData> {
        return context.sessionStorageDataStore.data
    }
}