package com.holparb.notemark.core.datasync.data

import com.holparb.notemark.core.datasync.domain.DataSyncRepository
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
import com.holparb.notemark.notes.domain.result.DataError
import com.holparb.notemark.notes.domain.result.DatabaseError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import timber.log.Timber

class DataSyncRepositoryImpl(
    private val noteDao: NoteDao,
    private val noteRemoteDataSource: NoteRemoteDataSource,
    private val userPreferences: UserPreferences,
) : DataSyncRepository {

    override suspend fun syncNotes(): Result<Unit, DataError> = withContext(Dispatchers.IO) {
        val syncEntities = try {
            noteDao.getSyncEntriesByUserId(userId = userPreferences.getUserId())
        } catch (e: Exception) {
            Timber.e("Failed to fetch sync table: ${e.message}")
            return@withContext Result.Error(
                DataError.LocalError(
                    error = DatabaseError.FETCH_FAILED
                )
            )
        }
        Timber.d("Syncing notes: $syncEntities")

        val semaphore = Semaphore(permits = 4)

        val results = supervisorScope {
            syncEntities.map { syncEntity ->
                async {
                    semaphore.withPermit {
                        syncNote(syncEntity)
                            .onSuccess {
                                Timber.d("Successfully synced note: $syncEntity")
                                noteDao.deleteSyncEntryById(syncEntity.id)
                            }
                    }
                }
            }.awaitAll()
        }

        combineResults(results)
    }

    override suspend fun fetchNotes(): Result<Unit, DataError> {
        when (val remoteResult = noteRemoteDataSource.getNotes()) {
            is Result.Error -> return Result.Error(DataError.RemoteError(remoteResult.error))
            is Result.Success -> {
                remoteResult.data.forEach { noteDto ->
                    val noteEntity = noteDto.toNoteEntity()
                    try {
                        val noteInDb = noteDao.getNote(noteEntity.noteId)
                        if (noteInDb == null || noteEntity.lastEditedAt > noteInDb.lastEditedAt) {
                            noteDao.upsertNote(noteEntity)
                        }
                    } catch (e: Exception) {
                        Timber.e("Database operation failed while fetching notes: ${e.message}")
                        return Result.Error(DataError.LocalError(DatabaseError.FETCH_FAILED))
                    }
                }
                return Result.Success(Unit)
            }
        }
    }

    private suspend fun createNoteRemote(note: Note): Result<Unit, DataError.RemoteError> {
        return when (val remoteResult = noteRemoteDataSource.createNote(note.toNoteDto())) {
            is Result.Error -> Result.Error(DataError.RemoteError(remoteResult.error))
            is Result.Success -> Result.Success(Unit)
        }
    }

    private suspend fun updateNoteRemote(note: Note): Result<Unit, DataError.RemoteError> {
        return when (val remoteResult = noteRemoteDataSource.updateNote(note.toNoteDto())) {
            is Result.Error -> Result.Error(DataError.RemoteError(remoteResult.error))
            is Result.Success -> Result.Success(Unit)
        }
    }

    private suspend fun deleteNoteRemote(noteId: String): Result<Unit, DataError.RemoteError> {
        return when (val remoteResult = noteRemoteDataSource.deleteNote(noteId)) {
            is Result.Error -> Result.Error(DataError.RemoteError(remoteResult.error))
            is Result.Success -> Result.Success(Unit)
        }
    }

    private suspend fun syncNote(syncEntity: SyncEntity): Result<Unit, DataError.RemoteError> {
        return when (syncEntity.operationType) {
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

    private fun <E : DataError> combineResults(results: List<Result<Unit, E>>): Result<Unit, E> {
        val firstError = results.firstOrNull { it is Result.Error } as? Result.Error<E>
        return firstError ?: Result.Success(Unit)
    }
}