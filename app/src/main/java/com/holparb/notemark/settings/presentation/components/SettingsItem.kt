package com.holparb.notemark.settings.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun SettingsItem(
    title: String,
    modifier: Modifier = Modifier,
    onItemClick: () -> Unit,
    subtitle: String? = null,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    subtitleColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
) {
    ListItem(
        modifier = modifier.clickable(
            onClick = onItemClick
        ) ,
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ),
        headlineContent = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(color = titleColor)
            )
        },
        supportingContent = {
            subtitle?.let {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(color = subtitleColor)
                )
            }
        },
        leadingContent = leadingContent,
        trailingContent = trailingContent
    )
}