package com.holparb.notemark.notes.data.mappers

import com.holparb.notemark.notes.data.database.NoteEntity
import com.holparb.notemark.notes.data.remote.NoteDto
import com.holparb.notemark.notes.domain.models.Note
import java.time.Instant

fun NoteEntity.toNote(): Note {
    return Note(
        noteId = noteId,
        title = title,
        content = content,
        createdAt = Instant.ofEpochMilli(createdAt),
        lastEditedAt = Instant.ofEpochMilli(lastEditedAt)
    )
}

fun Note.toNoteEntity(): NoteEntity {
    return NoteEntity(
        noteId = noteId,
        title = title,
        content = content,
        createdAt = createdAt.toEpochMilli(),
        lastEditedAt = lastEditedAt.toEpochMilli()
    )
}

fun NoteDto.toNoteEntity(): NoteEntity {
    return NoteEntity(
        noteId = id,
        title = title,
        content = content,
        createdAt = createdAt.
    )
}