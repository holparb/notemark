package com.holparb.notemark.notes.presentation.create_edit_note.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.holparb.notemark.R
import com.holparb.notemark.core.presentation.designsystem.theme.NoteMarkTheme

@Composable
fun CancelNoteDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        confirmButton =  {
            TextButton(
                onClick = onConfirm
            ) {
                Text(
                    text = stringResource(R.string.discard),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    text = stringResource(R.string.keep_editing)
                )
            }
        },
        title = {
            Text(
                text = stringResource(R.string.discard_changes)
            )
        },
        text = {
            Text(
                text = stringResource(R.string.cancel_dialog_text)
            )
        }
    )
}

@Preview
@Composable
private fun CancelNoteDialogPreview() {
    NoteMarkTheme {
        CancelNoteDialog(
            onConfirm = {},
            onDismiss = {}
        )
    }
}