package com.holparb.notemark.core.datasync.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.holparb.notemark.core.datasync.domain.DataSyncRepository

class NoteSyncWorker (
    private val dataSyncRepository: DataSyncRepository,
    appContext: Context,
    params: WorkerParameters
): CoroutineWorker(appContext = appContext, params = params) {

    override suspend fun doWork(): Result {
        val syncResult = dataSyncRepository.syncNotes()
        return when(syncResult) {
            is com.holparb.notemark.core.domain.result.Result.Error -> Result.retry()
            is com.holparb.notemark.core.domain.result.Result.Success -> Result.success()
        }
    }
}