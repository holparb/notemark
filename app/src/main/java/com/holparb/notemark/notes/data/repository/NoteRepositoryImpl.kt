package com.holparb.notemark.notes.data.repository

import com.holparb.notemark.core.domain.result.Result
import com.holparb.notemark.core.domain.result.onSuccess
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import timber.log.Timber
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
            val syncEntityForNote = noteDao.getSyncEntryByNoteId(note.noteId)
            val syncEntity = syncEntityForNote?.copy(
                payload = Json.encodeToString(note.toNoteEntity()),
                timestamp = Instant.now().toEpochMilli()
            ) ?:
            SyncEntity(
                id = UUID.randomUUID().toString(),
                noteId = note.noteId,
                userId = userPreferences.getUserId(),
                operationType = OperationType.UPDATE,
                payload = Json.encodeToString(note.toNoteEntity()),
                timestamp = Instant.now().toEpochMilli()
            )
            Timber.d("Updated sync entry: $syncEntity")
            noteDao.upsertNoteWithSync(note.toNoteEntity(), syncEntity)
            Result.Success(Unit)
        } catch(e: Exception) {
            Timber.e(e)
            Result.Error(DataError.LocalError(DatabaseError.UPSERT_FAILED)) 
        }
    }

    override suspend fun deleteNote(noteId: String): Result<Unit, DataError> {
        return try {
            // Check if there are any sync entries for the note
            val syncEntryForNote = noteDao.getSyncEntryByNoteId(noteId)
            if(syncEntryForNote == null ) {
                Timber.d("No entry for note, create delete entry")
                val syncEntity = SyncEntity(
                    id = UUID.randomUUID().toString(),
                    noteId = noteId,
                    userId = userPreferences.getUserId(),
                    operationType = OperationType.DELETE,
                    payload = noteId,
                    timestamp = Instant.now().toEpochMilli()
                )
                noteDao.deleteNoteWithSync(noteId, syncEntity)
            } else {
                when(syncEntryForNote.operationType) {
                    OperationType.CREATE -> {
                        Timber.d("Delete without sync")
                        noteDao.deleteNoteWithoutSync(noteId, syncEntryForNote.id)
                    }
                    OperationType.UPDATE -> {
                        Timber.d("Existing entry for note, modify to delete entry")
                        val syncEntity = syncEntryForNote.copy(
                            payload = noteId,
                            operationType = OperationType.DELETE
                        )
                        noteDao.deleteNoteWithSync(noteId, syncEntity)
                    }
                    OperationType.DELETE -> Unit
                }
            }
            Result.Success(Unit)
        } catch(e: Exception) {
            Timber.e(e)
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

    private suspend fun syncNote(syncEntity: SyncEntity): Result<Unit, DataError.RemoteError> {
        return when(syncEntity.operationType) {
            OperationType.CREATE -> {
                val note = Json.decodeFromString<NoteEntity>(syncEntity.payload).toNote()
                createNoteRemote(note)
            }
            OperationType.UPDATE -> {
                val note = Json.decodeFromString<NoteEntity>(syncEntity.payload).toNote()
                updateNoteRemote(note)
            }
            OperationType.DELETE -> {
                deleteNoteRemote(syncEntity.noteId)
            }
        }
    }

    override suspend fun syncNotes(): Result<Unit, DataError> = withContext(Dispatchers.IO) {
        val syncEntities = try {
            noteDao.getSyncEntriesByUserId(userId = userPreferences.getUserId())
        } catch (e: Exception) {
            Timber.e("Failed to fetch sync table: ${e.message}")
            return@withContext Result.Error(DataError.LocalError(error = DatabaseError.FETCH_FAILED))
        }
        Timber.d("Syncing notes: $syncEntities")

        val semaphore = Semaphore(permits = 4)

        val results = supervisorScope {
            syncEntities.map { syncEntity ->
                async {
                    semaphore.withPermit {
                        syncNote(syncEntity)
                            .onSuccess{
                                Timber.d("Successfully synced note: $syncEntity")
                                noteDao.deleteSyncEntryById(syncEntity.id)
                            }
                    }
                }
            }.awaitAll()
        }

        combineResults(results)
    }

    private fun <E : DataError> combineResults(results: List<Result<Unit, E>>): Result<Unit, E> {
        val firstError = results.firstOrNull { it is Result.Error } as? Result.Error<E>
        return firstError ?: Result.Success(Unit)
    }
}