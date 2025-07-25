package com.holparb.notemark.auth.presentation.register

import com.holparb.notemark.core.domain.result.NetworkError

sealed interface RegisterEvent {
    data object Success: RegisterEvent
    data class Failed(val error: NetworkError): RegisterEvent
}