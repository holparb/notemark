package com.holparb.notemark.auth.presentation.login

data class LoginState(
    val email: String = "",
    val isEmailValid: Boolean = false,
    val emailErrorMessage: String? = null,
    val password: String = "",
    val isPasswordValid: Boolean = false,
    val passwordErrorMessage: String? = null,
    val isLoginFormValid: Boolean = false
)