package com.holparb.notemark.notes.presentation.create_edit_note

sealed interface CreateEditNoteAction {
    data class NoteTitleChanged(val text: String): CreateEditNoteAction
    data class NoteContentChanged(val text: String): CreateEditNoteAction
    data object CancelNote: CreateEditNoteAction
    data object SaveNoteClicked: CreateEditNoteAction
    data object NoteCancelConfirmed: CreateEditNoteAction
    data object DismissCancelDialog: CreateEditNoteAction
}