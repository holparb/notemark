package com.holparb.notemark.app.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holparb.notemark.core.domain.session_storage.SessionStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class MainViewModel(
    private val sessionStorage: SessionStorage
): ViewModel() {

    private var hasLoadedInitialData = false

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn = _isLoggedIn.onStart {
        if (!hasLoadedInitialData) {
            observeSessionData()
            hasLoadedInitialData = true
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        false
    )

    private fun observeSessionData() {
        sessionStorage.observeSessionData().onEach { sessionData ->
            _isLoggedIn.update {
                sessionData.refreshToken != null
            }
        }.launchIn(viewModelScope)
    }
}