package com.holparb.notemark.notes.presentation.create_edit_note

sealed interface CreateEditNoteAction {
    data class NoteTitleChanged(val text: String): CreateEditNoteAction
    data class NoteContentChanged(val text: String): CreateEditNoteAction
}