package com.holparb.notemark.notes.data.mappers

import com.holparb.notemark.core.presentation.util.toISO8601DateTimeString
import com.holparb.notemark.notes.data.database.NoteEntity
import com.holparb.notemark.notes.data.remote.NoteDto
import com.holparb.notemark.notes.domain.models.Note
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

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

fun Note.toNoteDto(): NoteDto {
    return NoteDto(
        id = noteId,
        title = title,
        content = content,
        createdAt = createdAt.toISO8601DateTimeString(),
        lastEditedAt = createdAt.toISO8601DateTimeString()
    )
}

fun NoteDto.toNoteEntity(): NoteEntity {
    return NoteEntity(
        noteId = id,
        title = title,
        content = content,
        createdAt = convertISODateTimeStringToTimestamp(createdAt),
        lastEditedAt = convertISODateTimeStringToTimestamp(lastEditedAt)
    )
}

private fun convertISODateTimeStringToTimestamp(isoDateTimeString: String): Long {
    val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME.withZone(ZoneId.systemDefault())
    return try {
        ZonedDateTime.parse(isoDateTimeString, formatter).toInstant().toEpochMilli()
    } catch(e: DateTimeParseException) {
        0
    }
}