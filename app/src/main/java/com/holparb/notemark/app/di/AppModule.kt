package com.holparb.notemark.app.di

import com.holparb.notemark.app.NoteMarkApp
import com.holparb.notemark.app.presentation.MainViewModel
import com.holparb.notemark.core.data.networking.HttpClientFactory
import com.holparb.notemark.core.data.session_storage.SessionStorageDataStore
import com.holparb.notemark.core.data.user_preferences.UserPreferencesDataStore
import com.holparb.notemark.core.domain.session_storage.SessionStorage
import com.holparb.notemark.core.domain.user_preferences.UserPreferences
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single<CoroutineScope> {
        (androidApplication() as NoteMarkApp).applicationScope
    }
    singleOf(::UserPreferencesDataStore) bind UserPreferences::class
    singleOf(::SessionStorageDataStore) bind SessionStorage::class
    singleOf(::HttpClientFactory)
    singleOf(::MainViewModel)
    single<HttpClient> {
        get<HttpClientFactory>().create(CIO.create())
    }
}