package auth.presentation.register

sealed interface RegisterAction {
    data class OnUsernameChange(val text: String): RegisterAction
    data class OnEmailChange(val text: String): RegisterAction
    data class OnPasswordChange(val text: String): RegisterAction
    data class OnRepeatPasswordChange(val text: String): RegisterAction
    data object OnCreateAccountClick: RegisterAction
    data object OnLoginClick: RegisterAction
}