package notes.presentation.note_list

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.holparb.notemark.core.presentation.designsystem.theme.NoteMarkTheme

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
    Text("Note list")
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