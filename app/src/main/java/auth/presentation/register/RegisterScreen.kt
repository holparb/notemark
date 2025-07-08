package auth.presentation.register

import android.widget.Toast
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import auth.presentation.components.rootModifier
import auth.presentation.register.components.RegisterContentPhoneLandscape
import auth.presentation.register.components.RegisterContentPhonePortrait
import auth.presentation.register.components.RegisterContentTablet
import com.holparb.notemark.R
import com.holparb.notemark.core.presentation.designsystem.theme.NoteMarkTheme
import com.holparb.notemark.core.presentation.util.DeviceConfiguration
import com.holparb.notemark.core.presentation.util.ObserveAsEvents
import com.holparb.notemark.core.presentation.util.toString
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterRoot(
    viewModel: RegisterViewModel = koinViewModel<RegisterViewModel>(),
    navigateToLogin: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val context = LocalContext.current
    ObserveAsEvents(viewModel.events) { event ->
        when(event) {
            is RegisterEvent.Failed -> {
                Toast.makeText(
                    context,
                    event.error.toString(context),
                    Toast.LENGTH_LONG
                ).show()
            }

            RegisterEvent.Success -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.account_created),
                    Toast.LENGTH_LONG
                ).show()

                navigateToLogin()
            }
        }

    }

    RegisterScreen(
        state = state,
        onAction = { action ->
            when(action) {
                RegisterAction.OnLoginClick -> navigateToLogin()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun RegisterScreen(
    state: RegisterState,
    onAction: (RegisterAction) -> Unit,
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        contentWindowInsets = WindowInsets.statusBars
    ) { innerPadding ->
        val rootModifier = Modifier.rootModifier(innerPadding)
        when(deviceConfiguration) {
            DeviceConfiguration.PHONE_PORTRAIT -> {
                RegisterContentPhonePortrait(
                    modifier = rootModifier,
                    usernameText = state.username,
                    onUsernameChange = {
                        onAction(RegisterAction.OnUsernameChange(it))
                    },
                    isUsernameValid = state.isUserNameValid,
                    passwordText = state.password,
                    onPasswordChange = {
                        onAction(RegisterAction.OnPasswordChange(it))
                    },
                    isPasswordValid = state.isPasswordValid,
                    emailText = state.email,
                    onEmailChange = {
                        onAction(RegisterAction.OnEmailChange(it))
                    },
                    isEmailValid = state.isEmailValid,
                    repeatPasswordText = state.repeatPassword,
                    onRepeatPasswordChange = {
                        onAction(RegisterAction.OnRepeatPasswordChange(it))
                    },
                    isRepeatPasswordValid = state.isRepeatPasswordValid,
                    createAccountButtonEnabled = state.isRegisterFormValid,
                    onLoginLinkClick = {
                        onAction(RegisterAction.OnLoginClick)
                    },
                    onCreateAccountClick = {
                        onAction(RegisterAction.OnCreateAccountClick)
                    },
                    usernameErrorText = state.usernameErrorMessage,
                    emailErrorText = state.emailErrorMessage,
                    passwordErrorText = state.passwordErrorMessage,
                    repeatPasswordErrorText = state.repeatPasswordErrorMessage,
                    onValidateUsername = {
                        onAction(RegisterAction.OnValidateUsername)
                    },
                    onValidateEmail = {
                        onAction(RegisterAction.OnValidateEmail)
                    },
                    onValidatePassword = {
                        onAction(RegisterAction.OnValidatePassword)
                    },
                    onValidateRepeatPassword = {
                        onAction(RegisterAction.OnValidateRepeatPassword)
                    },
                    isLoading = state.isLoading
                )
            }
            DeviceConfiguration.PHONE_LANDSCAPE -> {
                RegisterContentPhoneLandscape(
                    modifier = rootModifier.windowInsetsPadding(WindowInsets.navigationBars),
                    usernameText = state.username,
                    onUsernameChange = {
                        onAction(RegisterAction.OnUsernameChange(it))
                    },
                    isUsernameValid = state.isUserNameValid,
                    passwordText = state.password,
                    onPasswordChange = {
                        onAction(RegisterAction.OnPasswordChange(it))
                    },
                    isPasswordValid = state.isPasswordValid,
                    emailText = state.email,
                    onEmailChange = {
                        onAction(RegisterAction.OnEmailChange(it))
                    },
                    isEmailValid = state.isEmailValid,
                    repeatPasswordText = state.repeatPassword,
                    onRepeatPasswordChange = {
                        onAction(RegisterAction.OnRepeatPasswordChange(it))
                    },
                    isRepeatPasswordValid = state.isRepeatPasswordValid,
                    createAccountButtonEnabled = state.isRegisterFormValid,
                    onLoginLinkClick = {
                        onAction(RegisterAction.OnLoginClick)
                    },
                    onCreateAccountClick = {
                        onAction(RegisterAction.OnCreateAccountClick)
                    },
                    usernameErrorText = state.usernameErrorMessage,
                    emailErrorText = state.emailErrorMessage,
                    passwordErrorText = state.passwordErrorMessage,
                    repeatPasswordErrorText = state.repeatPasswordErrorMessage,
                    onValidateUsername = {
                        onAction(RegisterAction.OnValidateUsername)
                    },
                    onValidateEmail = {
                        onAction(RegisterAction.OnValidateEmail)
                    },
                    onValidatePassword = {
                        onAction(RegisterAction.OnValidatePassword)
                    },
                    onValidateRepeatPassword = {
                        onAction(RegisterAction.OnValidateRepeatPassword)
                    },
                    isLoading = state.isLoading
                )
            }
            DeviceConfiguration.TABLET_PORTRAIT,
            DeviceConfiguration.TABLET_LANDSCAPE -> {
                RegisterContentTablet(
                    modifier = if(deviceConfiguration == DeviceConfiguration.TABLET_PORTRAIT) {
                        rootModifier
                    } else {
                        rootModifier.verticalScroll(rememberScrollState())
                    },
                    usernameText = state.username,
                    onUsernameChange = {
                        onAction(RegisterAction.OnUsernameChange(it))
                    },
                    isUsernameValid = state.isUserNameValid,
                    passwordText = state.password,
                    onPasswordChange = {
                        onAction(RegisterAction.OnPasswordChange(it))
                    },
                    isPasswordValid = state.isPasswordValid,
                    emailText = state.email,
                    onEmailChange = {
                        onAction(RegisterAction.OnEmailChange(it))
                    },
                    isEmailValid = state.isEmailValid,
                    repeatPasswordText = state.repeatPassword,
                    onRepeatPasswordChange = {
                        onAction(RegisterAction.OnRepeatPasswordChange(it))
                    },
                    isRepeatPasswordValid = state.isRepeatPasswordValid,
                    createAccountButtonEnabled = state.isRegisterFormValid,
                    onLoginLinkClick = {
                        onAction(RegisterAction.OnLoginClick)
                    },
                    onCreateAccountClick = {
                        onAction(RegisterAction.OnCreateAccountClick)
                    },
                    usernameErrorText = state.usernameErrorMessage,
                    emailErrorText = state.emailErrorMessage,
                    passwordErrorText = state.passwordErrorMessage,
                    repeatPasswordErrorText = state.repeatPasswordErrorMessage,
                    onValidateUsername = {
                        onAction(RegisterAction.OnValidateUsername)
                    },
                    onValidateEmail = {
                        onAction(RegisterAction.OnValidateEmail)
                    },
                    onValidatePassword = {
                        onAction(RegisterAction.OnValidatePassword)
                    },
                    onValidateRepeatPassword = {
                        onAction(RegisterAction.OnValidateRepeatPassword)
                    },
                    isLoading = state.isLoading
                )
            }
            DeviceConfiguration.UNKNOWN -> Unit
        }
    }
}

@Preview(
    showSystemUi = true,
    name = "Phone Portrait",
    device = Devices.PHONE,
    widthDp = 360,
    heightDp = 740
)
@Composable
private fun RegisterScreenPreview() {
    NoteMarkTheme {
        RegisterScreen(
            state = RegisterState(),
            onAction = {}
        )
    }
}