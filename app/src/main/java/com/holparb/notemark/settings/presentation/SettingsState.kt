package com.holparb.notemark.settings.presentation

data class SettingsState(
    val selectedSyncInterval: Int = 0,
    val lastSync: String = "",
    val isDropdownVisible: Boolean = false,
    val syncOptions: List<Int> = listOf(0, 15, 30, 45, 60)
)