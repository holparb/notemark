package com.holparb.notemark.auth.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.holparb.notemark.R
import com.holparb.notemark.auth.presentation.components.AuthHeaderSection
import com.holparb.notemark.auth.presentation.components.rootModifier
import com.holparb.notemark.auth.presentation.login.components.LoginFormSection
import com.holparb.notemark.core.domain.result.NetworkError
import com.holparb.notemark.core.presentation.designsystem.theme.NoteMarkTheme
import com.holparb.notemark.core.presentation.util.DeviceConfiguration
import com.holparb.notemark.core.presentation.util.ObserveAsEvents
import com.holparb.notemark.core.presentation.util.toString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginRoot(
    viewModel: LoginViewModel = koinViewModel<LoginViewModel>(),
    navigateToCreateAccount: () -> Unit,
    navigateToNoteList: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LoginScreen(
        state = state,
        events = viewModel.events,
        navigateToNoteList = navigateToNoteList,
        onAction = { action ->
            when(action) {
                LoginAction.OnCreateAccountLinkClick -> navigateToCreateAccount()
                else -> Unit
            }
            viewModel.onAction(action)
        },
    )
}

@Composable
fun LoginScreen(
    state: LoginState,
    events: Flow<LoginEvent>,
    onAction: (LoginAction) -> Unit,
    navigateToNoteList: () -> Unit,
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    ObserveAsEvents(events, snackbarHostState) { event ->
        when(event) {
            is LoginEvent.LoginError -> {
                snackbarHostState.currentSnackbarData?.dismiss()
                var snackbarMessage = event.error.toString(context)
                var actionLabel: String? = null
                when(event.error) {
                    NetworkError.UNAUTHORIZED -> {
                        snackbarMessage = context.getString(R.string.invalid_login_credentials)
                    }
                    else -> {
                        actionLabel = context.getString(R.string.try_again)
                    }
                }
                scope.launch {
                    val result = snackbarHostState.showSnackbar(
                        message = snackbarMessage,
                        actionLabel = actionLabel,
                        duration = SnackbarDuration.Long
                    )

                    if(result == SnackbarResult.ActionPerformed) {
                        onAction(LoginAction.OnLoginClick)
                    }
                }
            }
            is LoginEvent.LoginSuccessful -> {
                navigateToNoteList()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier
            .fillMaxSize(),
        contentWindowInsets = WindowInsets.systemBars
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
                        isLoading = state.isLoading
                    )
                }
            }
            DeviceConfiguration.PHONE_LANDSCAPE -> {
                Row(
                    modifier = Modifier
                        .rootModifier(innerPadding)
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
                        loginButtonEnabled = state.isLoginFormValid,
                        isLoading = state.isLoading
                    )
                }
            }
            DeviceConfiguration.TABLET_PORTRAIT,
            DeviceConfiguration.TABLET_LANDSCAPE -> {
                Column(
                    modifier = Modifier
                        .rootModifier(innerPadding)
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
                        isLoading = state.isLoading
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
            onAction = {},
            events = emptyFlow(),
            navigateToNoteList = {}
        )
    }
}