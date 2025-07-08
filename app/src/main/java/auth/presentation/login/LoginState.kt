package auth.presentation.login

data class LoginState(
    val email: String = "",
    val isEmailValid: Boolean = false,
    val emailErrorMessage: String = "",
    val password: String = "",
    val isLoginFormValid: Boolean = false
)