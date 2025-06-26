package com.holparb.notemark.core.presentation.designsystem.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.holparb.notemark.core.presentation.designsystem.theme.NoteMarkTheme

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = Color.Transparent
) {
    val borderColor = if(enabled) {
        MaterialTheme.colorScheme.primary
    } else {
        Color.Transparent
    }
    val buttonContainerColor = if(enabled) {
        containerColor
    } else {
        Color.Transparent
    }
    val buttonContentColor = if(enabled) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
        enabled = enabled,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = buttonContainerColor,
            contentColor = buttonContentColor,
        ),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            1.dp,
            borderColor
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Preview
@Composable
private fun SecondaryButtonPreview() {
    NoteMarkTheme {
        SecondaryButton(
            text = "Label",
            containerColor = MaterialTheme.colorScheme.secondary,
            onClick = {},
            enabled = true
        )
    }
}