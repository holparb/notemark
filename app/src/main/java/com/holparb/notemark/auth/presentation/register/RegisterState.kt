package com.holparb.notemark.auth.presentation.register

data class RegisterState(
    val username: String = "",
    val password: String = "",
    val isUserNameValid: Boolean = false,
    val usernameErrorMessage: String? = null,
    val isPasswordValid: Boolean = false,
    val passwordErrorMessage: String? = null,
    val email: String = "",
    val isEmailValid: Boolean = false,
    val emailErrorMessage: String? = null,
    val repeatPassword: String = "",
    val isRepeatPasswordValid: Boolean = false,
    val repeatPasswordErrorMessage: String? = null,
    val isRegisterFormValid: Boolean = false,
    val isLoading: Boolean = false
)