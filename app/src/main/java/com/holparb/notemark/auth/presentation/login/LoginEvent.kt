package com.holparb.notemark.auth.presentation.login

import com.holparb.notemark.core.domain.result.NetworkError

sealed interface LoginEvent {
    data class LoginSuccessful(val username: String): LoginEvent
    data class LoginError(val error: NetworkError): LoginEvent
}