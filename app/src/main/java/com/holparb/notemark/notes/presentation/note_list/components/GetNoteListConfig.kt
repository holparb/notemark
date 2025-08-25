package com.holparb.notemark.notes.presentation.note_list.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.holparb.notemark.core.presentation.util.DeviceConfiguration
import com.holparb.notemark.notes.presentation.note_list.models.NoteListConfig

@Composable
fun getNoteListConfig(deviceConfiguration: DeviceConfiguration): NoteListConfig {
    return when(deviceConfiguration) {
        DeviceConfiguration.PHONE_PORTRAIT -> NoteListConfig(
            numberOfColumns = 2,
            maxCharactersDisplayed = 150,
            contentPadding = PaddingValues(16.dp)
        )
        DeviceConfiguration.PHONE_LANDSCAPE -> NoteListConfig(
            numberOfColumns = 3,
            maxCharactersDisplayed = 150,
            contentPadding = PaddingValues(16.dp),
            windowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal)
        )
        DeviceConfiguration.TABLET_PORTRAIT,
        DeviceConfiguration.TABLET_LANDSCAPE,
        DeviceConfiguration.UNKNOWN -> NoteListConfig(
            numberOfColumns = 2,
            maxCharactersDisplayed = 250,
            contentPadding = PaddingValues(top = 16.dp, start = 24.dp, end = 24.dp, bottom = 16.dp)
        )
    }
}