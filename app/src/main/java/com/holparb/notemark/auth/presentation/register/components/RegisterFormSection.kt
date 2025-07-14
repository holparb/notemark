package com.holparb.notemark.auth.presentation.register.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.holparb.notemark.R
import com.holparb.notemark.auth.presentation.components.TextLink
import com.holparb.notemark.core.presentation.designsystem.buttons.PrimaryButton
import com.holparb.notemark.core.presentation.designsystem.textinputs.NoteMarkTextInput
import com.holparb.notemark.core.presentation.designsystem.theme.NoteMarkTheme

@Composable
fun RegisterFormSection(
    usernameText: String,
    onUsernameChange: (String) -> Unit,
    isUsernameValid: Boolean,
    emailText: String,
    onEmailChange: (String) -> Unit,
    isEmailValid: Boolean,
    passwordText: String,
    onPasswordChange: (String) -> Unit,
    isPasswordValid: Boolean,
    repeatPasswordText: String,
    onRepeatPasswordChange: (String) -> Unit,
    isRepeatPasswordValid: Boolean,
    onCreateAccountClick: () -> Unit,
    onLoginLinkClick: () -> Unit,
    createAccountButtonEnabled: Boolean,
    modifier: Modifier = Modifier,
    usernameErrorText: String? = null,
    emailErrorText: String? = null,
    passwordErrorText: String? = null,
    repeatPasswordErrorText: String? = null,
    onValidateUsername: () -> Unit,
    onValidateEmail: () -> Unit,
    onValidatePassword: () -> Unit,
    onValidateRepeatPassword: () -> Unit,
    isLoading: Boolean
) {
    var isUsernameInputFocused by remember {
        mutableStateOf(false)
    }

    var isEmalInputFocused by remember {
        mutableStateOf(false)
    }

    var isPasswordInputFocused by remember {
        mutableStateOf(false)
    }

    var isRepeatPasswordInputFocused by remember {
        mutableStateOf(false)
    }

    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        NoteMarkTextInput(
            text = usernameText,
            onTextValueChange = onUsernameChange,
            supportingText = stringResource(R.string.registration_username_supporting_text),
            hintText = stringResource(R.string.username_hint),
            labelText = stringResource(R.string.username),
            isError = usernameText.isNotBlank() && !isUsernameValid && !isUsernameInputFocused,
            errorText = usernameErrorText,
            isSupportingTextVisible = isUsernameInputFocused,
            modifier = Modifier.onFocusChanged { focusState ->
                isUsernameInputFocused = focusState.isFocused
                if(!focusState.isFocused) {
                    onValidateUsername()
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        NoteMarkTextInput(
            text = emailText,
            onTextValueChange = onEmailChange,
            hintText = stringResource(R.string.email_hint),
            labelText = stringResource(R.string.email),
            isError = emailText.isNotBlank() && !isEmailValid && !isEmalInputFocused,
            errorText = emailErrorText,
            modifier = Modifier.onFocusChanged { focusState ->
                isEmalInputFocused = focusState.isFocused
                if(!focusState.isFocused) {
                    onValidateEmail()
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        NoteMarkTextInput(
            text = passwordText,
            onTextValueChange = onPasswordChange,
            supportingText = stringResource(R.string.registration_password_supporting_text),
            hintText = stringResource(R.string.password),
            labelText = stringResource(R.string.password),
            isError = passwordText.isNotBlank() && !isPasswordValid && !isPasswordInputFocused,
            errorText = passwordErrorText,
            isSupportingTextVisible = isPasswordInputFocused,
            modifier = Modifier.onFocusChanged { focusState ->  
                isPasswordInputFocused = focusState.isFocused
                if(!focusState.isFocused) {
                    onValidatePassword()
                }
            },
            isInputSecret = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        NoteMarkTextInput(
            text = repeatPasswordText,
            onTextValueChange = onRepeatPasswordChange,
            hintText = stringResource(R.string.password),
            labelText = stringResource(R.string.repeat_password),
            isError = repeatPasswordText.isNotBlank() && !isRepeatPasswordValid && !isRepeatPasswordInputFocused,
            errorText = repeatPasswordErrorText,
            modifier = Modifier.onFocusChanged { focusState ->
                isRepeatPasswordInputFocused = focusState.isFocused
                if(!focusState.isFocused) {
                    onValidateRepeatPassword()
                }
            },
            isInputSecret = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            )
        )
        Spacer(modifier = Modifier.height(24.dp))
        PrimaryButton(
            text = stringResource(R.string.create_account),
            onClick = onCreateAccountClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = createAccountButtonEnabled,
            isLoading = isLoading
        )
        Spacer(modifier = Modifier.height(24.dp))
        TextLink(
            text = stringResource(R.string.sign_in_link_text),
            onClick = onLoginLinkClick,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun RegisterFormSectionPreview() {
    NoteMarkTheme {
        RegisterFormSection(
            usernameText = "John Doe",
            onUsernameChange = {},
            emailText = "asd@mail.com",
            onEmailChange = {},
            passwordText = "",
            repeatPasswordText = "",
            onPasswordChange = {},
            onRepeatPasswordChange = {},
            onLoginLinkClick = {},
            onCreateAccountClick = {},
            isPasswordValid = true,
            isUsernameValid = true,
            isRepeatPasswordValid = true,
            isEmailValid = true,
            createAccountButtonEnabled = true,
            usernameErrorText = "Username invalid",
            passwordErrorText = "Password invalid",
            emailErrorText = "Email invalid",
            repeatPasswordErrorText = "Password must match",
            onValidateUsername = {},
            onValidatePassword = {},
            onValidateEmail = {},
            onValidateRepeatPassword = {},
            isLoading = false
        )
    }
}