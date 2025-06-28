package auth.presentation.login

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.holparb.notemark.core.presentation.designsystem.theme.NoteMarkTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginRoot(
    viewModel: LoginViewModel = koinViewModel<LoginViewModel>()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LoginScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
) {
    Text(
        text = "Login screen"
    )
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