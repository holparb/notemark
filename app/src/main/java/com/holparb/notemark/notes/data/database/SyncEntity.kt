package com.holparb.notemark.notes.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sync_queue")
data class SyncEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val noteId: String,
    val operationType: OperationType,
    val payload: String,
    val timestamp: Long
)
