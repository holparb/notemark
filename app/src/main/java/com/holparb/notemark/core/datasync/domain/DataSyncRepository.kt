package com.holparb.notemark.core.datasync.domain

import com.holparb.notemark.core.domain.result.Result
import com.holparb.notemark.notes.domain.result.DataError

interface DataSyncRepository {

    suspend fun syncNotes(): Result<Unit, DataError>

    suspend fun fetchNotes(): Result<Unit, DataError>
}