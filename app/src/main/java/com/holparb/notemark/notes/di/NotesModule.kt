package com.holparb.notemark.notes.di

import com.holparb.notemark.notes.presentation.note_list.NoteListViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val notesModule = module {
    viewModelOf(::NoteListViewModel)
}