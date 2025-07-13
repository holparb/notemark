package app.di

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import app.NoteMarkApp
import com.holparb.notemark.core.domain.user_preferences.UserPreferences
import com.holparb.notemark.core.domain.user_preferences.UserPreferencesSerializer
import com.holparb.notemark.core.data.networking.HttpClientFactory
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

private const val USER_PREFS_DATA_STORE_FILE_NAME = "user_prefs.pb"

val appModule = module {
    single<CoroutineScope> {
        (androidApplication() as NoteMarkApp).applicationScope
    }
    single<HttpClient> {
        HttpClientFactory.create(CIO.create())
    }
    single<DataStore<UserPreferences>> {
        DataStoreFactory.create(
            serializer = UserPreferencesSerializer,
            produceFile = { androidApplication().dataStoreFile(USER_PREFS_DATA_STORE_FILE_NAME) },
            corruptionHandler = null,
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )
    }
}