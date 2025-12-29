package com.holparb.notemark.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class SettingsViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(SettingsState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = SettingsState()
        )

    fun onAction(action: SettingsAction) {
        when (action) {
            SettingsAction.DataSyncClick -> syncData()
            SettingsAction.DropdownDismissed -> toggleDropdownVisible()
            SettingsAction.LogOutClick -> logOut()
            SettingsAction.SyncIntervalClick -> toggleDropdownVisible()
            is SettingsAction.SyncOptionSelected -> selectSyncOption(action.selectedOption)
        }
    }

    private fun syncData() {

    }

    private fun logOut() {

    }


    private fun selectSyncOption(selectedOption: Int) {
        _state.update {
            it.copy(
                selectedSyncInterval = selectedOption,
                isDropdownVisible = false
            )
        }
    }

    private fun toggleDropdownVisible() {
        _state.update { it.copy(isDropdownVisible = !state.value.isDropdownVisible) }
    }

}