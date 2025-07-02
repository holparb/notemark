package auth.presentation.register

data class RegisterState(
    val username: String = "",
    val password: String = "",
    val isUserNameValid: Boolean = true,
    val isPasswordValid: Boolean = true,
    val email: String = "",
    val isEmailValid: Boolean = true,
    val repeatPassword: String = "",
    val isRepeatPasswordValid: Boolean = true,
    val isRegisterFormValid: Boolean = false,
    val isLoading: Boolean = false
)