package com.holparb.notemark.app

import android.app.Application
import com.holparb.notemark.BuildConfig
import com.holparb.notemark.app.di.appModule
import com.holparb.notemark.auth.di.authModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import com.holparb.notemark.notes.di.notesModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class NoteMarkApp: Application() {
    val applicationScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        if(BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidContext(this@NoteMarkApp)
            modules(
                appModule,
                authModule,
                notesModule
            )
        }
    }
}