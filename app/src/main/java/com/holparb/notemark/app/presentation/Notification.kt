package com.holparb.notemark.app.presentation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

object NotificationChannels {

    const val DATA_SYNC = "data_sync_channel"
}

object NotificationChannelFactory {

    fun create(context: Context) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channels = listOf(
            NotificationChannel(
                NotificationChannels.DATA_SYNC,
                "Data sync",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Data sync in the background"
            }
        )

        notificationManager.createNotificationChannels(channels)
    }
}
