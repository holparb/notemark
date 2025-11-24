package com.holparb.notemark.notes.data.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.holparb.notemark.notes.domain.repository.NoteRepository

class NoteSyncWorker (
    private val noteRepository: NoteRepository,
    appContext: Context,
    params: WorkerParameters
): CoroutineWorker(appContext = appContext, params = params) {

    override suspend fun doWork(): Result {
        val syncResult = noteRepository.syncNotes()
        return when(syncResult) {
            is com.holparb.notemark.core.domain.result.Result.Error -> Result.retry()
            is com.holparb.notemark.core.domain.result.Result.Success -> Result.success()
        }
    }
}