package com.holparb.notemark.core.presentation.designsystem.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.holparb.notemark.core.presentation.designsystem.theme.NoteMarkTheme
import com.holparb.notemark.core.presentation.designsystem.theme.buttonGradient

@Composable
fun GradientBackgroundFab(
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = Color.Transparent,
        elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = MaterialTheme.colorScheme.buttonGradient,
                    shape = RoundedCornerShape(10.dp)
                ),
            contentAlignment = Alignment.Center,
        ) {
            icon.invoke()
        }
    }

}

@Preview
@Composable
private fun GradientBackgroundFabPreview() {
    NoteMarkTheme {
        GradientBackgroundFab(
            icon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.White
                )
            },
            onClick = {},
            modifier = Modifier.size(64.dp)
        )
    }
}