package auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import auth.domain.form_validator.LoginFormValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class LoginViewModel(
    private val loginFormValidator: LoginFormValidator
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(LoginState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = LoginState()
        )

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnEmailChange -> TODO()
            LoginAction.OnLoginClick -> TODO()
            is LoginAction.OnPasswordChange -> TODO()
            is LoginAction.ValidateEmail -> TODO()
            is LoginAction.ValidatePassword -> TODO()
            LoginAction.OnCreateAccountLinkClick -> Unit
        }
    }

}