package app

import android.app.Application
import app.di.appModule
import auth.di.authModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import notes.di.notesModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NoteMarkApp: Application() {
    val applicationScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    override fun onCreate() {
        super.onCreate()

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