package com.holparb.notemark.settings.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.holparb.notemark.R
import com.holparb.notemark.core.presentation.designsystem.theme.NoteMarkTheme

@Composable
fun DropdownOptionsMenu(
    options: List<Int>,
    selectedOption: Int,
    onDismiss: () -> Unit,
    onSelectOption: (Int) -> Unit,
    modifier: Modifier = Modifier,
    maxDropdownHeight: Dp = Dp.Unspecified,
    dropdownOffset: IntOffset = IntOffset.Zero,
) {
    Popup(
        offset = dropdownOffset,
        onDismissRequest = onDismiss
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainerLowest,
            shape = RoundedCornerShape(16.dp),
            shadowElevation = 4.dp,
            modifier = modifier
                .heightIn(max = maxDropdownHeight)
        ) {
            LazyColumn(
                modifier = Modifier
                    .width(200.dp)
                    .animateContentSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(options) { option ->
                    val optionText = if(option == 0) {
                        stringResource(R.string.manual_only)
                    } else {
                        stringResource(R.string.sync_minutes, option)
                    }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    onClick = {
                                        onSelectOption(option)
                                    }
                                ),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = optionText,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            if(selectedOption == option) {
                                Icon(
                                    modifier = Modifier.size(20.dp),
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                }
            }
        }
    }
}

@Preview
@Composable
private fun DropdownOptionsMenuPreview() {
    NoteMarkTheme {
        DropdownOptionsMenu(
            options = listOf(0, 15, 30, 45, 60),
            selectedOption = 15,
            onSelectOption = {},
            onDismiss = {}

        )
    }
}