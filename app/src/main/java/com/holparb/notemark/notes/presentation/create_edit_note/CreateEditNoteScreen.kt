package com.holparb.notemark.notes.presentation.create_edit_note

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.holparb.notemark.R
import com.holparb.notemark.core.presentation.designsystem.theme.NoteMarkTheme
import com.holparb.notemark.core.presentation.util.DeviceConfiguration
import com.holparb.notemark.core.presentation.util.ObserveAsEvents
import com.holparb.notemark.core.presentation.util.toString
import com.holparb.notemark.notes.domain.result.DataError
import com.holparb.notemark.notes.presentation.components.NoteDialog
import com.holparb.notemark.notes.presentation.create_edit_note.components.CreateEditNote
import com.holparb.notemark.notes.presentation.create_edit_note.components.PortraitTopBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreateEditNoteRoot(
    viewModel: CreateEditNoteViewModel = koinViewModel(),
    navigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ObserveAsEvents(viewModel.events) { event ->
        when(event) {
            CreateEditNoteEvent.CancelSuccess,
            CreateEditNoteEvent.CreateUpdateSuccess -> navigateBack()
            is CreateEditNoteEvent.Error -> {
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

    CreateEditNoteScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun CreateEditNoteScreen(
    state: CreateEditNoteState,
    onAction: (CreateEditNoteAction) -> Unit,
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    BackHandler(
        enabled = !state.cancelDialogVisible
    ) {
        onAction(CreateEditNoteAction.CancelNote)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.systemBars,
    ) { innerPadding ->
        when(deviceConfiguration) {
            DeviceConfiguration.PHONE_PORTRAIT -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                ) {
                    PortraitTopBar(
                        onCancelClick = {
                            onAction(CreateEditNoteAction.CancelNote)
                        },
                        onSaveClick = {
                            onAction(CreateEditNoteAction.SaveNoteClicked)
                        }
                    )
                    CreateEditNote(
                        title = state.title,
                        content = state.content,
                        onTitleChange = {
                            onAction(CreateEditNoteAction.NoteTitleChanged(it))
                        },
                        onContentChange = {
                            onAction(CreateEditNoteAction.NoteContentChanged(it))
                        }
                    )
                }
            }
            DeviceConfiguration.PHONE_LANDSCAPE -> {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                ) {
                    CreateEditNote(
                        title = state.title,
                        content = state.content,
                        onTitleChange = {
                            onAction(CreateEditNoteAction.NoteTitleChanged(it))
                        },
                        onContentChange = {
                            onAction(CreateEditNoteAction.NoteContentChanged(it))
                        }
                    )
                }
            }
            DeviceConfiguration.TABLET_PORTRAIT,
            DeviceConfiguration.TABLET_LANDSCAPE,
            DeviceConfiguration.UNKNOWN -> {

            }
        }
        if(state.cancelDialogVisible) {
            NoteDialog(
                onConfirm = {
                    onAction(CreateEditNoteAction.NoteCancelConfirmed)
                },
                onDismiss = {
                    onAction(CreateEditNoteAction.DismissCancelDialog)
                },
                title = stringResource(R.string.discard_changes),
                text = stringResource(R.string.cancel_dialog_text),
                dismissButtonText = stringResource(R.string.discard),
                confirmButtonText = stringResource(R.string.keep_editing)
            )
        }
    }
}

@Preview
@Composable
private fun CreateEditNoteScreenPreview() {
    NoteMarkTheme {
        CreateEditNoteScreen(
            state = CreateEditNoteState(
                title = "Note Title",
                content = "This is some content for the note"
            ),
            onAction = {}
        )
    }
}