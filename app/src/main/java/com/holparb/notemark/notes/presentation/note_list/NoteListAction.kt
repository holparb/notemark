package com.holparb.notemark.notes.presentation.note_list

sealed interface NoteListAction {
    data class NoteClick(val noteId: String): NoteListAction
    data class NoteLongClick(val noteId: String): NoteListAction
    data object CreateNoteClick: NoteListAction
    data object DeleteDialogDismiss: NoteListAction
    data object DeleteConfirm: NoteListAction
}