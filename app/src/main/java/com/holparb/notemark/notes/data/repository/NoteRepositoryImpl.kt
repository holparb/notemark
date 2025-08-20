package com.holparb.notemark.notes.data.repository

import com.holparb.notemark.core.domain.result.Result
import com.holparb.notemark.core.domain.result.onError
import com.holparb.notemark.core.domain.result.onSuccess
import com.holparb.notemark.notes.data.database.NoteDao
import com.holparb.notemark.notes.data.mappers.toNote
import com.holparb.notemark.notes.data.remote.NoteRemoteDataSource
import com.holparb.notemark.notes.domain.models.Note
import com.holparb.notemark.notes.domain.repository.NoteRepository
import com.holparb.notemark.notes.domain.result.DataError
import com.holparb.notemark.notes.domain.result.DatabaseError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NoteRepositoryImpl(
    private val noteDao: NoteDao,
    private val noteRemoteDataSource: NoteRemoteDataSource
): NoteRepository {
    override suspend fun getNotes(page: Int, size: Int): Result<List<Note>, DataError> {
        val remoteResult = noteRemoteDataSource.getNotes(page = page, size = size)
        remoteResult.onError { error ->
            return Result.Error(DataError.RemoteError(error))
        }
        remoteResult.onSuccess { data ->
            val noteEntities = data.map { it.to }
            noteDao.upsertNotes()
        }
    }

    override fun observeNotes(): Result<Flow<Note>, DataError.LocalError> {
        return try {
            Result.Success(
                noteDao.observeNotes().map { it.toNote() }
            )
        } catch (e: Exception) {
            Result.Error(DataError.LocalError(DatabaseError.FETCH_FAILED))
        }
    }

    override suspend fun createNote(note: Note): Result<Unit, DataError> {
        TODO("Not yet implemented")
    }

    override suspend fun updateNote(note: Note): Result<Unit, DataError> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteNote(noteId: String): Result<Unit, DataError> {
        TODO("Not yet implemented")
    }

}