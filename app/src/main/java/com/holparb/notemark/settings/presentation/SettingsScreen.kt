package com.holparb.notemark.settings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.holparb.notemark.R
import com.holparb.notemark.core.presentation.designsystem.theme.Clock
import com.holparb.notemark.core.presentation.designsystem.theme.LogOut
import com.holparb.notemark.core.presentation.designsystem.theme.NoteMarkTheme
import com.holparb.notemark.core.presentation.designsystem.theme.Refresh
import com.holparb.notemark.settings.presentation.components.DropdownOptionsMenu
import com.holparb.notemark.settings.presentation.components.SettingsItem
import java.util.Locale.getDefault

@Composable
fun SettingsRoot(
    viewModel: SettingsViewModel = viewModel(),
    navigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SettingsScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    state: SettingsState,
    onAction: (SettingsAction) -> Unit,
) {
    var dropDownOffset by remember {
        mutableStateOf(IntOffset.Zero)
    }

    Scaffold(
        contentWindowInsets = WindowInsets.systemBars,
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(16.dp),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                ),
                title = {
                    Text(
                        text = stringResource(R.string.settings).uppercase(getDefault()),
                        style = MaterialTheme.typography.headlineSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                },
                navigationIcon = {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                        contentDescription = stringResource(R.string.navigate_back),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            SettingsItem(
                modifier = Modifier.onGloballyPositioned {
                    dropDownOffset = IntOffset(
                        x = (it.size.width * 0.6).toInt(),
                        y = it.positionInRoot().y.toInt() + it.size.height
                    )
                },
                onItemClick = {
                    onAction(SettingsAction.SyncIntervalClick)
                },
                title = stringResource(R.string.sync_interval),
                leadingContent = {
                    Icon(
                        imageVector = Icons.Outlined.Clock,
                        contentDescription = null
                    )
                },
                trailingContent = {
                    Row {
                        val selectedSyncIntervalText = if(state.selectedSyncInterval == 0) {
                            stringResource(R.string.manual_only)
                        } else {
                            stringResource(R.string.sync_minutes, state.selectedSyncInterval)
                        }
                        Text(
                            text = selectedSyncIntervalText,
                            style = MaterialTheme.typography.titleSmall
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                            contentDescription = null
                        )
                    }
                }
            )
            HorizontalDivider()
            SettingsItem(
                onItemClick = {
                    onAction(SettingsAction.DataSyncClick)
                },
                title = stringResource(R.string.sync_data),
                subtitle = stringResource(R.string.last_sync, state.lastSync),
                leadingContent = {
                    Icon(
                        imageVector = Icons.Outlined.Refresh,
                        contentDescription = null
                    )
                },
            )
            HorizontalDivider()
            SettingsItem(
                onItemClick = {
                    onAction(SettingsAction.LogOutClick)
                },
                title = stringResource(R.string.log_out),
                titleColor = MaterialTheme.colorScheme.error,
                leadingContent = {
                    Icon(
                        imageVector = Icons.Outlined.LogOut,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                },
            )
        }
        if(state.isDropdownVisible) {
            DropdownOptionsMenu(
                options = state.syncOptions,
                selectedOption = state.selectedSyncInterval,
                onDismiss = {
                    onAction(SettingsAction.DropdownDismissed)
                },
                onSelectOption = { selectedOption ->
                    onAction(SettingsAction.SyncOptionSelected(selectedOption))
                },
                dropdownOffset = dropDownOffset,
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    NoteMarkTheme {
        SettingsScreen(
            state = SettingsState(
                lastSync = "15"
            ),
            onAction = {}
        )
    }
}