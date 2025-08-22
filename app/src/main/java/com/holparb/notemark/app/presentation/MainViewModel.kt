package com.holparb.notemark.app.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holparb.notemark.core.domain.session_storage.SessionStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val sessionStorage: SessionStorage
): ViewModel() {

    private var hasLoadedInitialData = false

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn = _isLoggedIn.onStart {
        if (!hasLoadedInitialData) {
            loadInitialSessionData()
            observeSessionData()
            hasLoadedInitialData = true
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        false
    )

    private val _isLoadingSessionData = MutableStateFlow(true)
    val isLoadingSessionData = _isLoadingSessionData.asStateFlow()

    private fun loadInitialSessionData() {
        _isLoadingSessionData.update { true }
        viewModelScope.launch {
            _isLoggedIn.update {
                sessionStorage.getSessionData().refreshToken != null
            }
            // Add small delay to make sure states are updated properly by the time the nav host is initialized
            delay(50)
            _isLoadingSessionData.update { false }
        }
    }

    private fun observeSessionData() {
        sessionStorage.observeSessionData().onEach { sessionData ->
            _isLoggedIn.update {
                sessionData.refreshToken != null
            }
        }.launchIn(viewModelScope)
    }
}