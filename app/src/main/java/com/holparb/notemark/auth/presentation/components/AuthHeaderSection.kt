package com.holparb.notemark.auth.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.holparb.notemark.core.presentation.designsystem.theme.NoteMarkTheme

@Composable
fun AuthHeaderSection(
    mainText: String,
    subText: String,
    modifier: Modifier = Modifier,
    centerText: Boolean = false
) {
    Column(
        modifier = modifier,
        horizontalAlignment = if(centerText) {
            Alignment.CenterHorizontally
        } else {
            Alignment.Start
        }
    ) {
        Text(
            text = mainText,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = subText,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}

@Preview
@Composable
private fun RegisterHeaderSectionPreview() {
    NoteMarkTheme {
        AuthHeaderSection(
            mainText = "Main Text",
            subText = "Some additional text"
        )
    }
}