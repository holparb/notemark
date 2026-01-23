package com.holparb.notemark.core.datasync.data

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
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
        ).setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        ).setBackoffCriteria(
            backoffPolicy = BackoffPolicy.LINEAR,
            duration = Duration.ofMinutes(2)
        ).build()

        WorkManager.getInstance(appContext).enqueue(request)
    }

    override fun syncNotesNow() {
        val request = OneTimeWorkRequestBuilder<NoteSyncWorker>().setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        ).setExpedited(policy = OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST).build()

        WorkManager.getInstance(appContext).enqueue(request)
    }
}