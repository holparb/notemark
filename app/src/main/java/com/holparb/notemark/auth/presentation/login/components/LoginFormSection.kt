package com.holparb.notemark.auth.presentation.login.components

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
fun LoginFormSection(
    emailText: String,
    onEmailChange: (String) -> Unit,
    passwordText: String,
    onPasswordChange: (String) -> Unit,
    isEmailValid: Boolean,
    onLoginClick: () -> Unit,
    onCreateAccountLinkClick: () -> Unit,
    onValidateEmail: () -> Unit,
    onValidatePassword: () -> Unit,
    loginButtonEnabled: Boolean,
    modifier: Modifier = Modifier,
    emailErrorMessage: String? = null,
    isLoading: Boolean
) {
    var isEmailInputFocused by remember {
        mutableStateOf(false)
    }

    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        NoteMarkTextInput(
            text = emailText,
            labelText = stringResource(R.string.email),
            onTextValueChange = onEmailChange,
            isError = emailText.isNotBlank() && !isEmailInputFocused && !isEmailValid,
            errorText = emailErrorMessage,
            hintText = stringResource(R.string.email_hint),
            modifier = Modifier.onFocusChanged { focusState ->
                isEmailInputFocused = focusState.isFocused
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
            hintText = stringResource(R.string.password),
            labelText = stringResource(R.string.password),
            modifier = Modifier.onFocusChanged { focusState ->
                if(!focusState.isFocused) {
                    onValidatePassword()
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
            text = stringResource(R.string.log_in),
            onClick = onLoginClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = loginButtonEnabled,
            isLoading = isLoading
        )
        Spacer(modifier = Modifier.height(24.dp))
        TextLink(
            text = stringResource(R.string.create_account_link_text),
            onClick = onCreateAccountLinkClick,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun LoginFormSectionPreview() {
    NoteMarkTheme {
        LoginFormSection(
            emailText = "some text",
            passwordText = "password",
            onValidatePassword = {},
            onValidateEmail = {},
            onLoginClick = {},
            onEmailChange = {},
            onPasswordChange = {},
            onCreateAccountLinkClick = {},
            isEmailValid = false,
            emailErrorMessage = "Invalid email",
            loginButtonEnabled = true,
            isLoading = false
        )
    }
}