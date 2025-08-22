package com.holparb.notemark.notes.presentation.note_list

import com.holparb.notemark.notes.presentation.models.NoteUi

data class NoteListState(
    val notes: List<NoteUi> = emptyList(),
    val userInitials: String = "",
    val isLoading: Boolean = false
)