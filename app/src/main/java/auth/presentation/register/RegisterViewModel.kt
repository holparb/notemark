package auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import auth.domain.form_validator.RegistrationFormValidator
import auth.domain.repository.AuthRepository
import com.holparb.notemark.core.domain.result.onError
import com.holparb.notemark.core.domain.result.onSuccess
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val registrationFormValidator: RegistrationFormValidator,
    private val authRepository: AuthRepository
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(RegisterState())
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
            initialValue = RegisterState()
        )

    private val _events = Channel<RegisterEvent>()
    val events = _events.receiveAsFlow()

    private val _isUsernameValid = MutableStateFlow(false)
    private val _isEmailValid = MutableStateFlow(false)
    private val _isPasswordValid = MutableStateFlow(false)
    private val _isRepeatPasswordValid = MutableStateFlow(false)

    fun onAction(action: RegisterAction) {
        when (action) {
            RegisterAction.OnCreateAccountClick -> createAccount()
            is RegisterAction.OnEmailChange -> onEmailChange(action.text)
            is RegisterAction.OnPasswordChange -> onPasswordChange(action.text)
            is RegisterAction.OnRepeatPasswordChange -> onRepeatPasswordChange(action.text)
            is RegisterAction.OnUsernameChange -> onUsernameChange(action.text)
            is RegisterAction.OnValidateEmail -> validateEmail()
            is RegisterAction.OnValidatePassword -> validatePassword()
            is RegisterAction.OnValidateRepeatPassword -> validateRepeatPassword()
            is RegisterAction.OnValidateUsername -> validateUsername()
            RegisterAction.OnLoginClick -> Unit
        }
    }

    private fun observeFormValidation() {
        combine(
            _isUsernameValid,
            _isEmailValid,
            _isPasswordValid,
            _isRepeatPasswordValid
        ) { isUsernameValid, isEmailValid, isPasswordValid, isRepeatPasswordValid ->
            _state.update {
                it.copy(
                    isRegisterFormValid = isUsernameValid && isEmailValid
                            && isPasswordValid && isRepeatPasswordValid
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun validateUsername() {
        val validationResult = registrationFormValidator.validateUsername(state.value.username)
        _state.update {
            it.copy(
                isUserNameValid = validationResult.isValid,
                usernameErrorMessage = validationResult.errorMessage
            )
        }
        _isUsernameValid.update { validationResult.isValid }
    }

    private fun validateRepeatPassword() {
        val validationResult = registrationFormValidator.validateRepeatPassword(
            password = state.value.password,
            repeatPassword = state.value.repeatPassword
        )
        _state.update {
            it.copy(
                isRepeatPasswordValid = validationResult.isValid,
                repeatPasswordErrorMessage = validationResult.errorMessage
            )
        }
        _isRepeatPasswordValid.update { validationResult.isValid }
    }

    private fun validatePassword() {
        val validationResult = registrationFormValidator.validatePassword(state.value.password)
        _state.update {
            it.copy(
                isPasswordValid = validationResult.isValid,
                passwordErrorMessage = validationResult.errorMessage
            )
        }
        _isPasswordValid.update { validationResult.isValid }
    }

    private fun validateEmail() {
        val validationResult = registrationFormValidator.validateEmail(state.value.email)
        _state.update {
            it.copy(
                isEmailValid = validationResult.isValid,
                emailErrorMessage = validationResult.errorMessage
            )
        }
        _isEmailValid.update { validationResult.isValid }
    }

    private fun onUsernameChange(text: String) {
        _state.update {
            it.copy(username = text)
        }
    }

    private fun onRepeatPasswordChange(text: String) {
        _state.update {
            it.copy(repeatPassword = text)
        }
    }

    private fun onPasswordChange(text: String) {
        _state.update {
            it.copy(password = text)
        }
    }

    private fun onEmailChange(text: String) {
        _state.update {
            it.copy(email = text)
        }
    }

    private fun createAccount() {
        _state.update {
            it.copy(isLoading = true)
        }

        viewModelScope.launch {
            authRepository.registerUser(
                username = state.value.username,
                email = state.value.email,
                password = state.value.password
            )
                .onSuccess {
                    _state.update {
                        it.copy(isLoading = false)
                    }
                    _events.send(RegisterEvent.Success)
                }
                .onError { error ->
                    _state.update {
                        it.copy(isLoading = false)
                    }
                    _events.send(RegisterEvent.Failed(error))
                }
        }
    }
}