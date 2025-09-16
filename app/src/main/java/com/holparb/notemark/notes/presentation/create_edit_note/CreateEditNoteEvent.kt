package com.holparb.notemark.notes.presentation.create_edit_note

import com.holparb.notemark.notes.domain.result.DataError

sealed interface CreateEditNoteEvent {
    data class Error(val dataError: DataError): CreateEditNoteEvent
    data object CreateUpdateSuccess: CreateEditNoteEvent
    data object CancelSuccess: CreateEditNoteEvent
}