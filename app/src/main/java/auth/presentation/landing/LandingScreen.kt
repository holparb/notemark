package auth.presentation.landing

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import auth.presentation.landing.components.LandingContentPhoneLandscape
import auth.presentation.landing.components.LandingContentPhonePortrait
import auth.presentation.landing.components.LandingContentTabletLandscape
import auth.presentation.landing.components.LandingContentTabletPortrait
import com.holparb.notemark.core.presentation.designsystem.theme.NoteMarkTheme
import com.holparb.notemark.core.presentation.util.DeviceConfiguration

@Composable
fun LandingScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(
       currentWindowAdaptiveInfo().windowSizeClass
    )
    when(deviceConfiguration) {
        DeviceConfiguration.PHONE_PORTRAIT -> {
            LandingContentPhonePortrait(
                onLoginClick = onNavigateToLogin,
                onRegisterClick = onNavigateToRegister
            )
        }
        DeviceConfiguration.PHONE_LANDSCAPE -> {
            LandingContentPhoneLandscape(
                onLoginClick = onNavigateToLogin,
                onRegisterClick = onNavigateToRegister
            )
        }
        DeviceConfiguration.TABLET_PORTRAIT -> {
            LandingContentTabletPortrait(
                onLoginClick = onNavigateToLogin,
                onRegisterClick = onNavigateToRegister
            )
        }
        DeviceConfiguration.TABLET_LANDSCAPE-> {
            LandingContentTabletLandscape(
                onLoginClick = onNavigateToLogin,
                onRegisterClick = onNavigateToRegister
            )
        }
        else -> Unit
    }
}

@Preview
@Composable
private fun LandingScreenPreview() {
    NoteMarkTheme {
        LandingScreen(
            onNavigateToRegister = {},
            onNavigateToLogin = {}
        )
    }
}

@Preview(name = "Phone Landscape", device = Devices.PHONE, widthDp = 850, heightDp = 400)
@Composable
private fun LandingContentLandscapePreview() {
    NoteMarkTheme {
        LandingScreen(
            onNavigateToRegister = {},
            onNavigateToLogin = {}
        )
    }
}

@Preview(name = "Tablet Portrait", device = Devices.TABLET, widthDp = 900, heightDp = 1200)
@Composable
private fun LandingContentTabletPreview() {
    NoteMarkTheme {
        LandingScreen(
            onNavigateToRegister = {},
            onNavigateToLogin = {}
        )
    }
}

@Preview(name = "Tablet Landscape", device = Devices.TABLET, widthDp = 1280, heightDp = 720)
@Composable
private fun LandingContentTabletLandscapePreview() {
    NoteMarkTheme {
        LandingScreen(
            onNavigateToRegister = {},
            onNavigateToLogin = {}
        )
    }
}