package com.holparb.notemark.core.datasync.data

import android.app.Notification
import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.holparb.notemark.R
import com.holparb.notemark.app.presentation.NotificationChannels
import com.holparb.notemark.core.datasync.domain.DataSyncRepository
import com.holparb.notemark.core.domain.user_preferences.UserPreferences
import kotlinx.coroutines.delay
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber
import java.time.Instant

class NoteSyncWorker (
    appContext: Context,
    params: WorkerParameters
): CoroutineWorker(appContext = appContext, params = params), KoinComponent {

    private val dataSyncRepository: DataSyncRepository by inject()
    private val userPreferences: UserPreferences by inject()

    private fun getForegroundInfo(context: Context): ForegroundInfo {
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)  {
            ForegroundInfo(
                1,
                createNotification(context = context),
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            )
        } else {
            ForegroundInfo(
                1,
                createNotification(context = context)
            )
        }
    }

    override suspend fun doWork(): Result {
        try {
            setForeground(getForegroundInfo(applicationContext))
        } catch (e: Exception) {
            Timber.e("Could not set worker in foreground: \n${e.message}")
            return Result.retry()
        }
        delay(1000)
        userPreferences.saveLastSyncTimestamp(Instant.now().toEpochMilli())
        return when(dataSyncRepository.syncNotes()) {
            is com.holparb.notemark.core.domain.result.Result.Error -> Result.failure()
            is com.holparb.notemark.core.domain.result.Result.Success -> {
                Result.success()
            }
        }
    }

    private fun createNotification(context: Context): Notification {
        return NotificationCompat.Builder(context, NotificationChannels.DATA_SYNC)
            .setContentTitle(context.getString(R.string.data_sync_notification_title))
            .setContentText(context.getString(R.string.data_sync_notification_text))
            .setSmallIcon(R.drawable.refresh)
            .setOngoing(true)
            .build()
    }
}