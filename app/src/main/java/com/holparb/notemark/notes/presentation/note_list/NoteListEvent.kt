package com.holparb.notemark.notes.presentation.note_list

import com.holparb.notemark.notes.domain.result.DataError

sealed interface NoteListEvent {
    data class NoteCreated(val noteId: String): NoteListEvent
    data class NoteListError(val dataError: DataError): NoteListEvent
}