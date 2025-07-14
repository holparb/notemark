package com.holparb.notemark.auth.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.holparb.notemark.R
import com.holparb.notemark.auth.presentation.components.AuthHeaderSection
import com.holparb.notemark.auth.presentation.components.rootModifier
import com.holparb.notemark.auth.presentation.login.components.LoginFormSection
import com.holparb.notemark.core.presentation.designsystem.theme.NoteMarkTheme
import com.holparb.notemark.core.presentation.util.DeviceConfiguration
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginRoot(
    viewModel: LoginViewModel = koinViewModel<LoginViewModel>(),
    navigateToCreateAccount: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LoginScreen(
        state = state,
        onAction = { action ->
            when(action) {
                LoginAction.OnCreateAccountLinkClick -> navigateToCreateAccount()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        contentWindowInsets = WindowInsets.statusBars
    ) { innerPadding ->
        when(deviceConfiguration) {
            DeviceConfiguration.PHONE_PORTRAIT -> {
                Column(
                    modifier = Modifier
                        .rootModifier(innerPadding)
                        .padding(horizontal = 16.dp, vertical = 32.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(40.dp)
                ) {
                    AuthHeaderSection(
                        mainText = stringResource(R.string.log_in),
                        subText = stringResource(R.string.capture_your_thoughts_and_ideas)
                    )
                    LoginFormSection(
                        emailText = state.email,
                        onEmailChange = {
                            onAction(LoginAction.OnEmailChange(it))
                        },
                        emailErrorMessage = state.emailErrorMessage,
                        isEmailValid = state.isEmailValid,
                        passwordText = state.password,
                        onPasswordChange = {
                            onAction(LoginAction.OnPasswordChange(it))
                        },
                        onValidateEmail = {
                            onAction(LoginAction.ValidateEmail)
                        },
                        onValidatePassword = {
                            onAction(LoginAction.ValidatePassword)
                        },
                        onLoginClick = {
                            onAction(LoginAction.OnLoginClick)
                        },
                        onCreateAccountLinkClick = {
                            onAction(LoginAction.OnCreateAccountLinkClick)
                        },
                        loginButtonEnabled = state.isLoginFormValid,
                    )
                }
            }
            DeviceConfiguration.PHONE_LANDSCAPE -> {
                Row(
                    modifier = Modifier.rootModifier(innerPadding)
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp, vertical = 32.dp)
                        .windowInsetsPadding(WindowInsets.displayCutout)
                ) {
                    AuthHeaderSection(
                        mainText = stringResource(R.string.log_in),
                        subText = stringResource(R.string.capture_your_thoughts_and_ideas),
                        modifier = Modifier.weight(1f)
                    )
                    LoginFormSection(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState()),
                        emailText = state.email,
                        onEmailChange = {
                            onAction(LoginAction.OnEmailChange(it))
                        },
                        emailErrorMessage = state.emailErrorMessage,
                        isEmailValid = state.isEmailValid,
                        passwordText = state.password,
                        onPasswordChange = {
                            onAction(LoginAction.OnPasswordChange(it))
                        },
                        onValidateEmail = {
                            onAction(LoginAction.ValidateEmail)
                        },
                        onValidatePassword = {
                            onAction(LoginAction.ValidatePassword)
                        },
                        onLoginClick = {
                            onAction(LoginAction.OnLoginClick)
                        },
                        onCreateAccountLinkClick = {
                            onAction(LoginAction.OnCreateAccountLinkClick)
                        },
                        loginButtonEnabled = state.isLoginFormValid
                    )
                }
            }
            DeviceConfiguration.TABLET_PORTRAIT,
            DeviceConfiguration.TABLET_LANDSCAPE -> {
                Column(
                    modifier = Modifier.rootModifier(innerPadding)
                        .padding(horizontal = 120.dp, vertical = 100.dp),
                    verticalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    AuthHeaderSection(
                        mainText = stringResource(R.string.log_in),
                        subText = stringResource(R.string.capture_your_thoughts_and_ideas),
                        centerText = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    LoginFormSection(
                        emailText = state.email,
                        onEmailChange = {
                            onAction(LoginAction.OnEmailChange(it))
                        },
                        emailErrorMessage = state.emailErrorMessage,
                        isEmailValid = state.isEmailValid,
                        passwordText = state.password,
                        onPasswordChange = {
                            onAction(LoginAction.OnPasswordChange(it))
                        },
                        onValidateEmail = {
                            onAction(LoginAction.ValidateEmail)
                        },
                        onValidatePassword = {
                            onAction(LoginAction.ValidatePassword)
                        },
                        onLoginClick = {
                            onAction(LoginAction.OnLoginClick)
                        },
                        onCreateAccountLinkClick = {
                            onAction(LoginAction.OnCreateAccountLinkClick)
                        },
                        loginButtonEnabled = state.isLoginFormValid,
                    )
                }
            }
            DeviceConfiguration.UNKNOWN -> Unit
        }
    }
}

@Preview
@Composable
private fun Preview() {
    NoteMarkTheme {
        LoginScreen(
            state = LoginState(),
            onAction = {}
        )
    }
}