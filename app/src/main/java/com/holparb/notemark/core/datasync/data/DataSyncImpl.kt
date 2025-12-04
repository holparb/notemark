package com.holparb.notemark.core.datasync.data

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.holparb.notemark.core.datasync.domain.DataSync
import java.time.Duration
import java.util.concurrent.TimeUnit

class DataSyncImpl(
    private val appContext: Context
) : DataSync {

    override fun enqueueNoteSync(repeatInterval: Long) {
        val request = PeriodicWorkRequestBuilder<NoteSyncWorker>(
            repeatInterval = repeatInterval,
            repeatIntervalTimeUnit = TimeUnit.MINUTES
        ).setBackoffCriteria(
            backoffPolicy = BackoffPolicy.LINEAR,
            duration = Duration.ofMinutes(2)
        ).build()

        WorkManager.getInstance(appContext).enqueue(request)
    }
}