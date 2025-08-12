package com.holparb.notemark.notes.data.remote

data class NotesResponseDto(
    val notes: List<NoteDto>,
    val total: Int
)
