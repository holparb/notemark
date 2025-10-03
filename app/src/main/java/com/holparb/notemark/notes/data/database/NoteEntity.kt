package com.holparb.notemark.notes.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey
    val noteId: String,
    val title: String,
    val content: String,
    val createdAt: Long,
    val lastEditedAt: Long
)