package com.holparb.notemark.notes.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class NotesResponseDto(
    val notes: List<NoteDto>,
    val total: Int
)
