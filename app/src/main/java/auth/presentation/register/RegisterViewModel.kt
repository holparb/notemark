package auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class RegisterViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(RegisterState())
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
            initialValue = RegisterState()
        )

    fun onAction(action: RegisterAction) {
        when (action) {
            RegisterAction.OnCreateAccountClick -> createAccount()
            is RegisterAction.OnEmailChange -> onEmailChange(action.text)
            is RegisterAction.OnPasswordChange -> onPasswordChange(action.text)
            is RegisterAction.OnRepeatPasswordChange -> onRepeatPasswordChange(action.text)
            is RegisterAction.OnUsernameChange -> onUsernameChange(action.text)
            else -> Unit
        }
    }

    private fun onUsernameChange(text: String) {
        _state.update {
            it.copy(
                username = text
            )
        }
    }

    private fun onRepeatPasswordChange(text: String) {
        _state.update {
            it.copy(
                repeatPassword = text,
                isRepeatPasswordValid = text == state.value.password
            )
        }
    }

    private fun onPasswordChange(text: String) {
        _state.update {
            it.copy(
                password = text
            )
        }
    }

    private fun onEmailChange(text: String) {
        _state.update {
            it.copy(
                email = text,
            )
        }
    }

    private fun createAccount() {
        TODO("Not yet implemented")
    }

}