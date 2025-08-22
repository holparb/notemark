package com.holparb.notemark.notes.di

import androidx.room.Room
import com.holparb.notemark.notes.data.database.NoteDao
import com.holparb.notemark.notes.data.database.NoteDatabase
import com.holparb.notemark.notes.data.remote.NoteRemoteDataSource
import com.holparb.notemark.notes.data.repository.NoteRepositoryImpl
import com.holparb.notemark.notes.domain.repository.NoteRepository
import com.holparb.notemark.notes.presentation.note_list.NoteListViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val notesModule = module {
    single<NoteDatabase> {
        Room.databaseBuilder(
            androidApplication(),
            NoteDatabase::class.java,
            "notes.db"
        ).build()
    }
    single<NoteDao> {
        get<NoteDatabase>().noteDao
    }
    singleOf(::NoteRemoteDataSource)
    singleOf(::NoteRepositoryImpl) bind NoteRepository::class

    viewModelOf(::NoteListViewModel)
}