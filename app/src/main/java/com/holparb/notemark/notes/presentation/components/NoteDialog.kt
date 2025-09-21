package com.holparb.notemark.notes.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.holparb.notemark.core.presentation.designsystem.theme.NoteMarkTheme

@Composable
fun NoteDialog(
    title: String,
    text: String,
    dismissButtonText: String,
    confirmButtonText: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        confirmButton =  {
            TextButton(
                onClick = {
                    onConfirm()
                }
            ) {
                Text(
                    text = confirmButtonText,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    text = dismissButtonText
                )
            }
        },
        title = {
            Text(
                text = title
            )
        },
        text = {
            Text(
                text = text
            )
        }
    )
}



@Preview
@Composable
private fun CancelNoteDialogPreview() {
    NoteMarkTheme {
        NoteDialog(
            onConfirm = {},
            onDismiss = {},
            title = "Are you sure?",
            text = "You have some changes you may lose.",
            confirmButtonText = "Yes",
            dismissButtonText = "No"
        )
    }
}