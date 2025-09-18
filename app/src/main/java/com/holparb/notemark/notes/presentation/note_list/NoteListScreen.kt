package com.holparb.notemark.notes.presentation.note_list

import android.widget.Toast
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.holparb.notemark.R
import com.holparb.notemark.core.presentation.designsystem.buttons.GradientBackgroundFab
import com.holparb.notemark.core.presentation.designsystem.theme.NoteMarkTheme
import com.holparb.notemark.core.presentation.util.DeviceConfiguration
import com.holparb.notemark.core.presentation.util.ObserveAsEvents
import com.holparb.notemark.core.presentation.util.toString
import com.holparb.notemark.notes.domain.result.DataError
import com.holparb.notemark.notes.presentation.models.NoteUi
import com.holparb.notemark.notes.presentation.note_list.components.EmptyNoteList
import com.holparb.notemark.notes.presentation.note_list.components.NoteList
import com.holparb.notemark.notes.presentation.note_list.components.NoteListTopBar
import com.holparb.notemark.notes.presentation.note_list.components.getNoteListConfig
import org.koin.androidx.compose.koinViewModel
import java.time.Instant

@Composable
fun NoteListRoot(
    viewModel: NoteListViewModel = koinViewModel<NoteListViewModel>(),
    navigateToCreateEditNote: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ObserveAsEvents(viewModel.events) { event ->
        when(event) {
            is NoteListEvent.NoteCreated -> navigateToCreateEditNote(event.noteId)
            is NoteListEvent.NoteListError -> {
                val errorText = when(event.dataError) {
                    is DataError.LocalError -> event.dataError.error.toString(context)
                    is DataError.RemoteError -> event.dataError.error.toString(context)
                }
                Toast.makeText(
                    context,
                    errorText,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    NoteListScreen(
        state = state,
        onAction = { action ->
            when(action) {
                is NoteListAction.NoteClick -> navigateToCreateEditNote(action.noteId)
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun NoteListScreen(
    state: NoteListState,
    onAction: (NoteListAction) -> Unit,
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)
    val fabEndPadding = when(deviceConfiguration) {
        DeviceConfiguration.PHONE_LANDSCAPE -> WindowInsets
            .navigationBars
            .asPaddingValues()
            .calculateRightPadding(LayoutDirection.Ltr)
        else -> 0.dp
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.systemBars,
        topBar = {
            NoteListTopBar(
                userInitials = state.userInitials
            )
        },
        floatingActionButton = {
            GradientBackgroundFab(
                modifier = Modifier
                    .padding(bottom = 12.dp, end = fabEndPadding)
                    .size(64.dp),
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
                modifier = Modifier.padding(innerPadding).consumeWindowInsets(WindowInsets.navigationBars),
                notes = state.notes,
                noteListConfig = getNoteListConfig(deviceConfiguration),
                onNoteClick = { noteId ->
                    onAction(NoteListAction.NoteClick(noteId))
                },
                onNoteLongClick = { noteId ->
                    onAction(NoteListAction.NoteLongClick(noteId))
                }
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun NoteListScreenPreview() {
    NoteMarkTheme {
        NoteListScreen(
            state = NoteListState(
                notes = (1..20).map {
                    NoteUi(
                        id = "ID$it",
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

@Preview(name = "Phone Landscape", device = Devices.PHONE, widthDp = 740, heightDp = 360, showSystemUi = true)
@Composable
private fun NoteListScreenLandscapePreview() {
    NoteMarkTheme {
        NoteListScreen(
            state = NoteListState(
                notes = (1..20).map {
                    NoteUi(
                        id = "ID$it",
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

@Preview(name = "Tablet Portrait", device = Devices.TABLET, widthDp = 900, heightDp = 1200, showSystemUi = true)
@Composable
private fun NoteListScreenTabletPreview() {
    NoteMarkTheme {
        NoteListScreen(
            state = NoteListState(
                notes = (1..20).map {
                    NoteUi(
                        id = "ID$it",
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