package com.holparb.notemark.notes.presentation.note_list.models

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets

data class NoteListConfig(
    val maxCharactersDisplayed: Int,
    val numberOfColumns: Int,
    val contentPadding: PaddingValues,
    val windowInsets: WindowInsets = WindowInsets(0, 0, 0, 0)
)
