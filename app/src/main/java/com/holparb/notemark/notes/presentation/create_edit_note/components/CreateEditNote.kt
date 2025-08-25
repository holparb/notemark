package com.holparb.notemark.notes.presentation.create_edit_note.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CreateEditNote(
    title: String,
    content: String,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        BasicTextField(
            value = title,
            onValueChange = onTitleChange,
            singleLine = true,
            textStyle = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(20.dp))
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))
        BasicTextField(
            value = content,
            onValueChange = onContentChange,
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}