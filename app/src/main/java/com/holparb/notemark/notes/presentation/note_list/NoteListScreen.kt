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
import com.holparb.notemark.notes.presentation.note_list.components.EmptyNoteList
import com.holparb.notemark.notes.presentation.note_list.components.NoteListTopBar

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

        }
    }
}

@Preview
@Composable
private fun Preview() {
    NoteMarkTheme {
        NoteListScreen(
            state = NoteListState(
                notes = emptyList(),
                userInitials = "PL"
            ),
            onAction = {}
        )
    }
}