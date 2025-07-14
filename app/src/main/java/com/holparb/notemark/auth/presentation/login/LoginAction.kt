package com.holparb.notemark.auth.presentation.login

sealed interface LoginAction {
    data class OnEmailChange(val text: String): LoginAction
    data class OnPasswordChange(val text: String): LoginAction
    data object ValidateEmail: LoginAction
    data object ValidatePassword: LoginAction
    data object OnLoginClick: LoginAction
    data object OnCreateAccountLinkClick: LoginAction
}