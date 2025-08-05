package com.holparb.notemark.notes.presentation.note_list

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.holparb.notemark.R
import com.holparb.notemark.core.presentation.designsystem.buttons.GradientBackgroundFab
import com.holparb.notemark.core.presentation.designsystem.theme.NoteMarkTheme
import com.holparb.notemark.core.presentation.util.DeviceConfiguration
import com.holparb.notemark.notes.presentation.models.NoteUi
import com.holparb.notemark.notes.presentation.note_list.components.EmptyNoteList
import com.holparb.notemark.notes.presentation.note_list.components.NoteList
import com.holparb.notemark.notes.presentation.note_list.components.NoteListTopBar
import java.time.Instant

@Composable
fun NoteListRoot(
    viewModel: NoteListViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    NoteListScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun NoteListScreen(
    state: NoteListState,
    onAction: (NoteListAction) -> Unit,
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.systemBars,
        topBar = {
            NoteListTopBar(
                userInitials = "PL"
            )
        },
        floatingActionButton = {
            GradientBackgroundFab(
                modifier = Modifier.size(64.dp),
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.add_note),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                },
                onClick = {
                    onAction(NoteListAction.CreateNoteClick)
                }
            )
        }
    ) { innerPadding ->
        if(state.notes.isEmpty()) {
            EmptyNoteList(Modifier.padding(innerPadding))
        } else {
            NoteList(
                modifier = Modifier.padding(innerPadding),
                notes = state.notes,
                numberOfColumns = when(deviceConfiguration) {
                    DeviceConfiguration.PHONE_PORTRAIT,
                    DeviceConfiguration.TABLET_PORTRAIT -> 2
                    DeviceConfiguration.PHONE_LANDSCAPE,
                    DeviceConfiguration.UNKNOWN,
                    DeviceConfiguration.TABLET_LANDSCAPE -> 3
                },
                maxTextCharactersDisplayed = when(deviceConfiguration) {
                    DeviceConfiguration.PHONE_PORTRAIT,
                    DeviceConfiguration.PHONE_LANDSCAPE -> 150
                    DeviceConfiguration.TABLET_PORTRAIT,
                    DeviceConfiguration.TABLET_LANDSCAPE,
                    DeviceConfiguration.UNKNOWN -> 250
                },
                onClick = { noteId ->
                    onAction(NoteListAction.NoteClick(noteId))
                },
                onLongClick = { noteId ->
                    onAction(NoteListAction.NoteLongClick(noteId))
                }
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    NoteMarkTheme {
        NoteListScreen(
            state = NoteListState(
                notes = (1..20).map {
                    NoteUi(
                        id = it,
                        title = "Note $it",
                        text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
                        createdAt = Instant.now()
                    )
                },
                userInitials = "PL"
            ),
            onAction = {}
        )
    }
}