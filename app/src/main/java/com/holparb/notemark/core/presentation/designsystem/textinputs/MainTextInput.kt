package com.holparb.notemark.core.presentation.designsystem.textinputs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.holparb.notemark.core.presentation.designsystem.theme.NoteMarkTheme

@Composable
fun MainTextInput(
    text: String,
    supportingText: String,
    hintText: String,
    labelText: String,
    onTextValueChange: (String) -> Unit,
    isError: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = labelText,
            style = MaterialTheme.typography.bodyMedium,
            color = if(enabled) {
                MaterialTheme.colorScheme.onSurface
            } else {
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            }
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = text,
            onValueChange = onTextValueChange,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            textStyle = MaterialTheme.typography.bodyLarge,
            shape = RoundedCornerShape(12.dp),
            colors = noteMarkTextFieldColors(),
            placeholder = {
                Text(
                    text = hintText,
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            supportingText = {
                Text(
                    text = supportingText,
                    style = MaterialTheme.typography.bodySmall
                )
            },
            isError = isError,
        )
    }
}

@Composable
fun noteMarkTextFieldColors(): TextFieldColors {
    return OutlinedTextFieldDefaults.colors(
        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
        focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
        errorPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
        disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        errorContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        disabledContainerColor = MaterialTheme.colorScheme.surface,
        unfocusedBorderColor = Color.Transparent,
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        errorBorderColor = MaterialTheme.colorScheme.error,
        disabledBorderColor = Color.Transparent,
        cursorColor = MaterialTheme.colorScheme.primary,
        errorCursorColor = MaterialTheme.colorScheme.error,
        unfocusedSupportingTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
        focusedSupportingTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
        errorSupportingTextColor = MaterialTheme.colorScheme.error,
        disabledSupportingTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
    )
}

@Preview
@Composable
private fun MainTextInputPreview() {
    NoteMarkTheme {
        MainTextInput(
            text = "Input",
            onTextValueChange = {},
            supportingText = "Supporting text",
            labelText = "Label",
            hintText = "Placeholder",
            isError = false,
            enabled = false
        )
    }
}