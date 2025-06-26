package com.holparb.notemark.core.presentation.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    secondary = PrimaryWithOpacity,
    background = Background,
    surface = Surface,
    surfaceContainerLowest = SurfaceLowest,
    onPrimary = OnPrimary,
    onSecondary = OnPrimaryWithOpacity,
    onSurface = OnSurface,
    onSurfaceVariant = OnSurfaceVariant,
    error = Error
)

@Composable
fun NoteMarkTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}