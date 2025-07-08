package auth.presentation.login

sealed interface LoginAction {
    data class OnEmailChange(val text: String): LoginAction
    data class OnPasswordChange(val text: String): LoginAction
    data class ValidateEmail(val email: String): LoginAction
    data class ValidatePassword(val password: String): LoginAction
    data object OnLoginClick: LoginAction
    data object OnCreateAccountLinkClick: LoginAction
}