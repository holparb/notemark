package com.holparb.notemark.notes.domain.repository

import com.holparb.notemark.core.domain.result.Result
import com.holparb.notemark.notes.domain.models.Note
import com.holparb.notemark.notes.domain.result.DataError
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    suspend fun getNotes(page: Int = -1, size: Int = 20): Result<List<Note>, DataError>
    fun observeNotes(): Result<Flow<Note>, DataError.LocalError>
    suspend fun createNote(note: Note): Result<Unit, DataError>
    suspend fun updateNote(note: Note): Result<Unit, DataError>
    suspend fun deleteNote(noteId: String): Result<Unit, DataError>
}