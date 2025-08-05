package com.holparb.notemark.notes.presentation.note_list

sealed interface NoteListAction {
    data class NoteClick(val noteId: Int): NoteListAction
    data class NoteLongClick(val noteId: Int): NoteListAction
    data object CreateNoteClick: NoteListAction
}