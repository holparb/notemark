package auth.presentation.landing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.holparb.notemark.core.presentation.designsystem.theme.NoteMarkTheme

@Composable
fun LandingRoot(
    viewModel: LandingViewModel = viewModel(),
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LandingScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun LandingScreen(
    state: LandingState,
    onAction: (LandingAction) -> Unit,
) {

}

@Preview
@Composable
private fun LandingScreenPreview() {
    NoteMarkTheme {
        LandingScreen(
            state = LandingState(),
            onAction = {}
        )
    }
}