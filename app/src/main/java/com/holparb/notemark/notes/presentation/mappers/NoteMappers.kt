package com.holparb.notemark.notes.presentation.mappers

import com.holparb.notemark.notes.domain.models.Note
import com.holparb.notemark.notes.presentation.models.NoteUi

fun Note.toNoteUi(): NoteUi {
    return NoteUi(
        id = noteId,
        title = title,
        text = content,
        createdAt = createdAt
    )
}