package app.di

import app.NoteMarkApp
import com.holparb.notemark.core.data.networking.HttpClientFactory
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {
    single<CoroutineScope> {
        (androidApplication() as NoteMarkApp).applicationScope
    }
    single<HttpClient> {
        HttpClientFactory.create(CIO.create())
    }
}