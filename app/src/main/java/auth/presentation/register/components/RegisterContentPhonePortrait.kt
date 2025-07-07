package auth.presentation.register.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import auth.presentation.components.rootModifier
import com.holparb.notemark.core.presentation.designsystem.theme.NoteMarkTheme

@Composable
fun RegisterContentPhonePortrait(
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
    Column(
        modifier = modifier
            .padding(top = 32.dp, start = 16.dp, end = 16.dp)
            .verticalScroll(rememberScrollState())
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(40.dp)
    ) {
        RegisterHeaderSection(
            modifier = Modifier.fillMaxWidth()
        )
        RegisterFormSection(
            usernameText = usernameText,
            onUsernameChange = onUsernameChange,
            isUsernameValid = isUsernameValid,
            emailText = emailText,
            onEmailChange = onEmailChange,
            isEmailValid = isEmailValid,
            passwordText = passwordText,
            onPasswordChange = onPasswordChange,
            isPasswordValid = isPasswordValid,
            repeatPasswordText = repeatPasswordText,
            onRepeatPasswordChange = onRepeatPasswordChange,
            isRepeatPasswordValid = isRepeatPasswordValid,
            onCreateAccountClick = onCreateAccountClick,
            onLoginLinkClick = onLoginLinkClick,
            usernameErrorText = usernameErrorText,
            emailErrorText = emailErrorText,
            passwordErrorText = passwordErrorText,
            repeatPasswordErrorText = repeatPasswordErrorText,
            onValidateUsername = onValidateUsername,
            onValidateEmail = onValidateEmail,
            onValidatePassword = onValidatePassword,
            onValidateRepeatPassword = onValidateRepeatPassword,
            createAccountButtonEnabled = createAccountButtonEnabled,
            isLoading = isLoading
        )
    }
}

@Preview
@Composable
private fun RegisterContentPhonePortraitPreview() {
    NoteMarkTheme {
        RegisterContentPhonePortrait(
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
            modifier = Modifier.rootModifier(innerPadding = PaddingValues(0.dp)),
            isLoading = false
        )
    }
}