package com.holparb.notemark.notes.presentation.models

import com.holparb.notemark.core.presentation.util.toReadableDate
import java.time.Instant

data class NoteUi(
    val id: Int,
    val title: String,
    val text: String,
    val createdAt: Instant
) {
    val formattedCreatedAt = createdAt.toReadableDate()
}
