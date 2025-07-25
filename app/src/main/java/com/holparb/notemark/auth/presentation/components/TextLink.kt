package com.holparb.notemark.auth.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign

@Composable
fun TextLink(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.titleSmall.copy(
        color = MaterialTheme.colorScheme.primary
    )
) {
    Text(
        text = text,
        style = textStyle,
        modifier = modifier
            .clickable(
                onClick = onClick
            ),
        textAlign = TextAlign.Center
    )
}