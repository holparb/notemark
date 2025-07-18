package com.holparb.notemark.core.presentation.designsystem.textinputs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.holparb.notemark.R
import com.holparb.notemark.core.presentation.designsystem.theme.Eye
import com.holparb.notemark.core.presentation.designsystem.theme.EyeOff
import com.holparb.notemark.core.presentation.designsystem.theme.NoteMarkTheme

@Composable
fun NoteMarkTextInput(
    text: String,
    labelText: String,
    onTextValueChange: (String) -> Unit,
    isError: Boolean,
    modifier: Modifier = Modifier,
    supportingText: String? = null,
    errorText: String? = null,
    isSupportingTextVisible: Boolean = false,
    hintText: String? = null,
    enabled: Boolean = true,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = true,
    isInputSecret: Boolean = false,
) {
    var showText by remember {
        mutableStateOf(false)
    }

    val supportingTextText = if(isError && errorText != null) {
        errorText
    } else {
        supportingText
    }

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
                if(hintText != null) {
                    Text(
                        text = hintText,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            },
            supportingText = {
                if((supportingText != null && isSupportingTextVisible) || (errorText != null && isError)) {
                    Text(
                        text = supportingTextText ?: "",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            },
            isError = isError,
            keyboardActions = keyboardActions,
            keyboardOptions = keyboardOptions,
            singleLine = singleLine,
            trailingIcon = {
                if(isInputSecret) {
                    IconButton(
                        onClick = {
                            showText = !showText
                        }
                    ) {
                        Icon(
                            imageVector = if (showText) {
                                Icons.Outlined.EyeOff
                            } else {
                                Icons.Outlined.Eye
                            },
                            contentDescription = if (showText) {
                                stringResource(R.string.hide_password)
                            } else {
                                stringResource(R.string.show_password)
                            }
                        )
                    }
                }
            },
            visualTransformation = if(isInputSecret && !showText) {
                PasswordVisualTransformation('*')
            } else {
                VisualTransformation.None
            }
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
        NoteMarkTextInput(
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