package com.holparb.notemark.settings.presentation

sealed interface SettingsAction {
    data object SyncIntervalClick: SettingsAction
    data object DataSyncClick: SettingsAction
    data object LogOutClick: SettingsAction
    data class SyncOptionSelected(val selectedOption: Int): SettingsAction
    data object DropdownDismissed: SettingsAction
}