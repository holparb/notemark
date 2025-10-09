package com.holparb.notemark.notes.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes ORDER BY createdAt DESC")
    suspend fun getNotes(): List<NoteEntity>

    @Query("SELECT * FROM notes ORDER BY createdAt DESC")
    fun observeNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE noteId = :noteId")
    suspend fun getNote(noteId: String): NoteEntity

    @Upsert
    suspend fun upsertNote(note: NoteEntity)

    @Upsert
    suspend fun upsertNotes(notes: List<NoteEntity>)

    @Query("DELETE FROM notes WHERE noteId = :noteId")
    suspend fun deleteNoteById(noteId: String)

    @Query("DELETE FROM notes")
    suspend fun clearNotes()

    @Query("SELECT * from sync_queue WHERE userId = :userId")
    suspend fun getSyncEntriesByUserId(userId: String): List<SyncEntity>

    @Query("SELECT * from sync_queue WHERE noteId = :noteId")
    suspend fun getSyncEntryByNoteId(noteId: String): SyncEntity?

    @Upsert
    suspend fun upsertSyncEntry(syncEntry: SyncEntity)

    @Query("DELETE from sync_queue WHERE id = :syncEntryId")
    suspend fun deleteSyncEntryById(syncEntryId: String)

    @Transaction
    suspend fun upsertNoteWithSync(note: NoteEntity, syncEntry: SyncEntity) {
        upsertNote(note)
        upsertSyncEntry(syncEntry)
    }

    @Transaction
    suspend fun deleteNoteWithSync(noteId: String, syncEntry: SyncEntity) {
        deleteNoteById(noteId)
        upsertSyncEntry(syncEntry)
    }

    @Transaction
    suspend fun deleteNoteWithoutSync(noteId: String, syncEntryId: String) {
        deleteSyncEntryById(syncEntryId)
        deleteNoteById(noteId)
    }
}