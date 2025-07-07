package notes.di

import notes.presentation.note_list.NoteListViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val notesModule = module {
    viewModelOf(::NoteListViewModel)
}