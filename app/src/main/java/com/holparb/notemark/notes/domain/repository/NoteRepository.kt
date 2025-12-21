package com.holparb.notemark.notes.domain.repository

import com.holparb.notemark.core.domain.result.Result
import com.holparb.notemark.notes.domain.models.Note
import com.holparb.notemark.notes.domain.result.DataError
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    fun observeNotes(): Result<Flow<List<Note>>, DataError.LocalError>
    suspend fun getNote(noteId: String): Result<Note, DataError.LocalError>
    suspend fun createNewNoteInDatabase(): Result<String, DataError.LocalError>
    suspend fun createNote(note: Note): Result<Unit, DataError>
    suspend fun updateNote(note: Note): Result<Unit, DataError>
    suspend fun deleteNote(noteId: String): Result<Unit, DataError>
    suspend fun deleteNoteFromDatabase(noteId: String): Result<Unit, DataError.LocalError>
}