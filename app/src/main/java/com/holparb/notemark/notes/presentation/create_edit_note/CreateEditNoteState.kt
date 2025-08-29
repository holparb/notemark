package com.holparb.notemark.notes.presentation.create_edit_note

data class CreateEditNoteState(
    val title: String = "",
    val content: String = "",
    val cancelDialogVisible: Boolean = false
)