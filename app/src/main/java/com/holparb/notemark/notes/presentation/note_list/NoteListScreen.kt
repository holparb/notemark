package com.holparb.notemark.notes.presentation.note_list

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.holparb.notemark.core.presentation.designsystem.theme.NoteMarkTheme
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
        modifier = Modifier
            .fillMaxSize(),
        contentWindowInsets = WindowInsets.systemBars,
        topBar = {
           NoteListTopBar(
               userInitials = "PL"
           )
        }
    ) { innerPadding ->
        Text("note list")
    }
}

@Preview
@Composable
private fun Preview() {
    NoteMarkTheme {
        NoteListScreen(
            state = NoteListState(),
            onAction = {}
        )
    }
}