package com.holparb.notemark.notes.data.repository

import com.holparb.notemark.core.domain.result.Result
import com.holparb.notemark.core.domain.user_preferences.UserPreferences
import com.holparb.notemark.notes.data.database.NoteDao
import com.holparb.notemark.notes.data.database.NoteEntity
import com.holparb.notemark.notes.data.database.OperationType
import com.holparb.notemark.notes.data.database.SyncEntity
import com.holparb.notemark.notes.data.mappers.toNote
import com.holparb.notemark.notes.data.mappers.toNoteDto
import com.holparb.notemark.notes.data.mappers.toNoteEntity
import com.holparb.notemark.notes.data.remote.NoteRemoteDataSource
import com.holparb.notemark.notes.domain.models.Note
import com.holparb.notemark.notes.domain.repository.NoteRepository
import com.holparb.notemark.notes.domain.result.DataError
import com.holparb.notemark.notes.domain.result.DatabaseError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import java.time.Instant
import java.util.UUID

class NoteRepositoryImpl(
    private val noteDao: NoteDao,
    private val noteRemoteDataSource: NoteRemoteDataSource,
    private val userPreferences: UserPreferences,
    private val applicationScope: CoroutineScope
): NoteRepository {
    override suspend fun getNotes(page: Int, size: Int): Result<Unit, DataError> {
        return when(val remoteResult = noteRemoteDataSource.getNotes(page = page, size = size)) {
            is Result.Error -> Result.Error(DataError.RemoteError(remoteResult.error))
            is Result.Success -> {
                val noteEntities = remoteResult.data.map { it.toNoteEntity() }
                try {
                    noteDao.upsertNotes(noteEntities)
                    Result.Success(Unit)
                } catch (e: Exception) {
                    Result.Error(DataError.LocalError(DatabaseError.UPSERT_FAILED))
                }
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

    override suspend fun getNote(noteId: String): Result<Note, DataError.LocalError> {
        return try {
            val note = noteDao.getNote(noteId)
            Result.Success(note.toNote())
        } catch (e: Exception) {
            Result.Error(DataError.LocalError(DatabaseError.FETCH_FAILED))
        }
    }

    override suspend fun createNewNoteInDatabase(): Result<String, DataError.LocalError> {
        val note = NoteEntity(
            noteId = UUID.randomUUID().toString(),
            title = "",
            content = "",
            createdAt = Instant.now().toEpochMilli(),
            lastEditedAt = Instant.now().toEpochMilli()
        )
        return try {
            noteDao.upsertNote(note)
            Result.Success(note.noteId)
        } catch(e: Exception) {
            Result.Error(DataError.LocalError(DatabaseError.UPSERT_FAILED))
        }
    }

    override suspend fun createNote(note: Note): Result<Unit, DataError> {
        return try {
            val syncEntity = SyncEntity(
                id = UUID.randomUUID().toString(),
                noteId = note.noteId,
                userId = userPreferences.getUserId(),
                operationType = OperationType.CREATE,
                payload = Json.encodeToString(note.toNoteEntity()),
                timestamp = Instant.now().toEpochMilli()
            )
            noteDao.upsertNoteWithSync(note.toNoteEntity(), syncEntity)
            Result.Success(Unit)
        } catch(e: Exception) {
            Result.Error(DataError.LocalError(DatabaseError.UPSERT_FAILED))
        }
    }

    override suspend fun updateNote(note: Note): Result<Unit, DataError> {
        return try {
            val syncEntity = SyncEntity(
                id = UUID.randomUUID().toString(),
                noteId = note.noteId,
                userId = userPreferences.getUserId(),
                operationType = OperationType.UPDATE,
                payload = Json.encodeToString(note.toNoteEntity()),
                timestamp = Instant.now().toEpochMilli()
            )
            noteDao.upsertNoteWithSync(note.toNoteEntity(), syncEntity)
            Result.Success(Unit)
        } catch(e: Exception) {
            Result.Error(DataError.LocalError(DatabaseError.UPSERT_FAILED))
        }
    }

    override suspend fun deleteNote(noteId: String): Result<Unit, DataError> {
        return try {
            // Check if there are any sync entries for the note
            val syncEntryForNote = noteDao.getSyncEntriesByNoteId(noteId)
            if(syncEntryForNote.isNotEmpty()) {
                // If there are sync entries we just need to delete them because the note is deleted before it would need to be synced.
                syncEntryForNote.forEach { syncEntity ->
                    noteDao.deleteNoteWithoutSync(noteId, syncEntity.id)
                }
            } else {
                val syncEntity = SyncEntity(
                    id = UUID.randomUUID().toString(),
                    noteId = noteId,
                    userId = userPreferences.getUserId(),
                    operationType = OperationType.DELETE,
                    payload = noteId,
                    timestamp = Instant.now().toEpochMilli()
                )
                noteDao.deleteNoteWithSync(noteId, syncEntity)
            }
            Result.Success(Unit)
        } catch(e: Exception) {
            Result.Error(DataError.LocalError(DatabaseError.DELETE_FAILED))
        }
    }

    override suspend fun deleteNoteFromDatabase(noteId: String): Result<Unit, DataError.LocalError> {
        return try {
            noteDao.deleteNoteById(noteId)
            Result.Success(Unit)
        } catch(e: Exception) {
            Result.Error(DataError.LocalError(DatabaseError.DELETE_FAILED))
        }
    }

    private suspend fun createNoteRemote(note: Note): Result<Unit, DataError.RemoteError> {
        val remoteResult = noteRemoteDataSource.createNote(note.toNoteDto())
        return when(remoteResult) {
            is Result.Error -> Result.Error(DataError.RemoteError(remoteResult.error))
            is Result.Success -> Result.Success(Unit)
        }
    }

    private suspend fun updateNoteRemote(note: Note): Result<Unit, DataError.RemoteError> {
        val remoteResult = noteRemoteDataSource.updateNote(note.toNoteDto())
        return when(remoteResult) {
            is Result.Error -> Result.Error(DataError.RemoteError(remoteResult.error))
            is Result.Success -> Result.Success(Unit)
        }
    }

    private suspend fun deleteNoteRemote(noteId: String): Result<Unit, DataError.RemoteError> {
        val remoteResult = noteRemoteDataSource.deleteNote(noteId)
        return when(remoteResult) {
            is Result.Error -> Result.Error(DataError.RemoteError(remoteResult.error))
            is Result.Success -> Result.Success(Unit)
        }
    }


}