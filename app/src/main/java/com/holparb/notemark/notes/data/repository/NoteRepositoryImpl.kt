package com.holparb.notemark.notes.data.repository

import com.holparb.notemark.core.domain.result.NetworkError
import com.holparb.notemark.core.domain.result.Result
import com.holparb.notemark.notes.data.database.NoteDao
import com.holparb.notemark.notes.data.database.NoteEntity
import com.holparb.notemark.notes.data.mappers.toNote
import com.holparb.notemark.notes.data.mappers.toNoteDto
import com.holparb.notemark.notes.data.mappers.toNoteEntity
import com.holparb.notemark.notes.data.remote.NoteRemoteDataSource
import com.holparb.notemark.notes.domain.models.Note
import com.holparb.notemark.notes.domain.repository.NoteRepository
import com.holparb.notemark.notes.domain.result.DataError
import com.holparb.notemark.notes.domain.result.DatabaseError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant

class NoteRepositoryImpl(
    private val noteDao: NoteDao,
    private val noteRemoteDataSource: NoteRemoteDataSource,
    private val applicationScope: CoroutineScope
): NoteRepository {
    override suspend fun getNotes(page: Int, size: Int): Result<List<Note>, DataError> {
        val remoteResult = noteRemoteDataSource.getNotes(page = page, size = size)
        return when(remoteResult) {
            is Result.Error -> Result.Error(DataError.RemoteError(remoteResult.error))
            is Result.Success -> {
                val noteEntities = remoteResult.data.map { it.toNoteEntity() }
                noteDao.upsertNotes(noteEntities)
                Result.Success(noteEntities.map { it.toNote() })
            }
        }
    }

    override fun observeNotes(): Result<Flow<List<Note>>, DataError.LocalError> {
        return try {
            Result.Success(
                noteDao.observeNotes().map { notes ->
                    notes.map { it.toNote() }
                }
            )
        } catch (e: Exception) {
            Result.Error(DataError.LocalError(DatabaseError.FETCH_FAILED))
        }
    }

    override suspend fun createNote(note: Note): Result<Unit, DataError> {
        try {
            noteDao.upsertNote(note.toNoteEntity())
        } catch(e: Exception) {
            return Result.Error(DataError.LocalError(DatabaseError.UPSERT_FAILED))
        }

        return applicationScope.async {
            val remoteResult = noteRemoteDataSource.createNote(note.toNoteDto())
            return@async when(remoteResult) {
                is Result.Error -> Result.Error(DataError.RemoteError(NetworkError.BAD_REQUEST))
                is Result.Success -> Result.Success(Unit)
            }
        }.await()
    }

    override suspend fun updateNote(note: Note): Result<Unit, DataError> {
        try {
            noteDao.upsertNote(note.toNoteEntity())
        } catch(e: Exception) {
            return Result.Error(DataError.LocalError(DatabaseError.UPSERT_FAILED))
        }

        return applicationScope.async {
            val remoteResult = noteRemoteDataSource.updateNote(note.toNoteDto())
            return@async when(remoteResult) {
                is Result.Error -> Result.Error(DataError.RemoteError(NetworkError.BAD_REQUEST))
                is Result.Success -> Result.Success(Unit)
            }
        }.await()
    }

    override suspend fun deleteNote(noteId: String): Result<Unit, DataError> {

        try {
            noteDao.deleteNoteById(noteId)
        } catch(e: Exception) {
            return Result.Error(DataError.LocalError(DatabaseError.DELETE_FAILED))
        }

        return applicationScope.async {
            val remoteResult = noteRemoteDataSource.deleteNote(noteId)
            return@async when(remoteResult) {
                is Result.Error -> Result.Error(DataError.RemoteError(NetworkError.BAD_REQUEST))
                is Result.Success -> Result.Success(Unit)
            }
        }.await()
    }
}