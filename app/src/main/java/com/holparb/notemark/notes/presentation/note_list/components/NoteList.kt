package com.holparb.notemark.notes.presentation.note_list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.holparb.notemark.core.presentation.designsystem.theme.NoteMarkTheme
import com.holparb.notemark.notes.presentation.models.NoteUi
import com.holparb.notemark.notes.presentation.note_list.models.NoteListConfig
import java.time.Instant

@Composable
fun NoteList(
    notes: List<NoteUi>,
    noteListConfig: NoteListConfig,
    onNoteClick: (String) -> Unit,
    onNoteLongClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalStaggeredGrid(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)
            .windowInsetsPadding(noteListConfig.windowInsets),
        columns = StaggeredGridCells.Fixed(noteListConfig.numberOfColumns),
        contentPadding = noteListConfig.contentPadding,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalItemSpacing = 16.dp
    ) {
        items(notes) { note ->
            NoteCard(
                note = note,
                maxTextCharactersDisplayed = noteListConfig.maxCharactersDisplayed,
                onNoteClick = onNoteClick,
                onNoteLongClick = onNoteLongClick
            )
        }
    }
}

@Preview
@Composable
private fun NoteListPreview() {
    NoteMarkTheme {
        NoteList(
            notes = (1..20).map {
                NoteUi(
                    id = "ID$it",
                    title = "Note $it",
                    text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
                    createdAt = Instant.now()
                )
            },
            NoteListConfig(
                numberOfColumns = 2,
                maxCharactersDisplayed = 150,
                contentPadding = PaddingValues(16.dp),
                windowInsets = WindowInsets.safeDrawing
            ),
            onNoteClick = {},
            onNoteLongClick = {},
        )
    }
}