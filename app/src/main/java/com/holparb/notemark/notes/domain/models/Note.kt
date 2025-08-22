package com.holparb.notemark.notes.domain.models

import java.time.Instant

data class Note(
    val noteId: String,
    val title: String,
    val content: String,
    val createdAt: Instant,
    val lastEditedAt: Instant
)
