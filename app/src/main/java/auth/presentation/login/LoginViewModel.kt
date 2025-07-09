package auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import auth.domain.form_validator.LoginFormValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class LoginViewModel(
    private val loginFormValidator: LoginFormValidator
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(LoginState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                observeFormValidation()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = LoginState()
        )

    private val _isEmailValid = MutableStateFlow(false)
    private val _isPasswordValid = MutableStateFlow(false)

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnEmailChange -> onEmailChange(action.text)
            is LoginAction.OnPasswordChange -> onPasswordChange(action.text)
            is LoginAction.ValidateEmail -> validateEmail()
            is LoginAction.ValidatePassword -> validatePassword()
            LoginAction.OnLoginClick -> login()
            LoginAction.OnCreateAccountLinkClick -> Unit
        }
    }

    private fun observeFormValidation() {
        combine(
            _isEmailValid,
            _isPasswordValid
        ) { isEmailValid, isPasswordValid ->
            _state.update {
                it.copy(isLoginFormValid = isEmailValid && isPasswordValid)
            }
        }.launchIn(viewModelScope)
    }

    private fun login() {
        TODO("Not yet implemented")
    }

    private fun validateEmail() {
        val validationResult = loginFormValidator.validateEmail(state.value.email)
        _state.update {
            it.copy(
                isEmailValid = validationResult.isValid,
                emailErrorMessage = validationResult.errorMessage
            )
        }
        _isEmailValid.update { validationResult.isValid }
    }

    private fun validatePassword() {
        val validationResult = loginFormValidator.validatePassword(state.value.password)
        _state.update {
            it.copy(
                isPasswordValid = validationResult.isValid,
                passwordErrorMessage = validationResult.errorMessage
            )
        }
        _isPasswordValid.update { validationResult.isValid }

    }

    private fun onEmailChange(text: String) {
        _state.update {
            it.copy(email = text)
        }
    }

    private fun onPasswordChange(text: String) {
        _state.update {
            it.copy(password = text)
        }
    }
}