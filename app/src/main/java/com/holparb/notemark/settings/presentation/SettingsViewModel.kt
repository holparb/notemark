package com.holparb.notemark.settings.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holparb.notemark.R
import com.holparb.notemark.core.datasync.domain.DataSync
import com.holparb.notemark.core.domain.user_preferences.UserPreferences
import com.holparb.notemark.core.presentation.util.toDateAndTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.Instant
import kotlin.math.abs

class SettingsViewModel(
    private val dataSync: DataSync,
    private val userPreferences: UserPreferences,
    private val appContext: Context
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(SettingsState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                observeUserPreferences()
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
            SettingsAction.BackNavigationClick -> Unit
        }
    }

    private fun observeUserPreferences() {
        userPreferences.observeLastSyncTimestamp().onEach { lastSync ->
            _state.update {
                it.copy(
                    lastSync = getLastSyncTimeAsString(lastSync)
                )
            }
        }.launchIn(viewModelScope)

        userPreferences.observeSyncInterval().onEach { syncInterval ->
            _state.update {
                it.copy(
                    selectedSyncInterval = syncInterval
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun syncData() {
        dataSync.syncNotesNow()
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

    private fun getLastSyncTimeAsString(lastSyncTimestamp: Long): String {
        if(lastSyncTimestamp.toInt() == -1) {
            return appContext.getString(R.string.never_synced)
        }

        val MINUTE_MS = 60_000L
        val HOUR_MS = 60 * MINUTE_MS
        val DAY_MS = 24 * HOUR_MS
        val FIVE_MINUTES_MS = 5 * MINUTE_MS
        val SEVEN_DAYS_MS = 7 * DAY_MS

        var difference = abs(Instant.now().toEpochMilli() - lastSyncTimestamp)

        return when {
            difference < FIVE_MINUTES_MS -> {
                return appContext.getString(R.string.just_now)
            }

            difference <= SEVEN_DAYS_MS -> {
                val days = difference / DAY_MS
                difference %= DAY_MS

                val hours = difference / HOUR_MS
                difference %= HOUR_MS

                val minutes = difference / MINUTE_MS

                if (hours.toInt() == 0) {
                    appContext.getString(R.string.minutes_ago, minutes)
                } else if (days.toInt() == 0) {
                    appContext.getString(R.string.hours_ago, hours)
                } else {
                    appContext.getString(R.string.days_ago, days)
                }
            }

            else -> {
                return Instant.ofEpochMilli(difference).toDateAndTime()
            }
        }
    }
}