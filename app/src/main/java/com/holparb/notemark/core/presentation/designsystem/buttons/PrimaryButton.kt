package com.holparb.notemark.core.presentation.designsystem.buttons

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.holparb.notemark.core.presentation.designsystem.theme.Copy
import com.holparb.notemark.core.presentation.designsystem.theme.NoteMarkTheme
import com.holparb.notemark.core.presentation.designsystem.theme.onSurfaceWithOpacity

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    enabled: Boolean = true
) {
    Button (
        modifier = modifier,
        onClick = onClick,
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if(enabled) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurfaceWithOpacity
            },
            contentColor = if(enabled) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onSurface
            }
        )
    ) {
        leadingIcon?.invoke()

        if(leadingIcon != null) {
            Spacer(modifier = Modifier.width(6.dp))
        }

        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall
        )

        if(trailingIcon != null) {
            Spacer(modifier = Modifier.width(6.dp))
        }

        trailingIcon?.invoke()
    }
}

@Preview
@Composable
private fun PrimaryButtonPreview() {
    NoteMarkTheme {
        PrimaryButton(
            text = "Label",
            trailingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Copy,
                    contentDescription = null
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Copy,
                    contentDescription = null
                )
            },
            enabled = true,
            onClick = {}
        )
    }
}