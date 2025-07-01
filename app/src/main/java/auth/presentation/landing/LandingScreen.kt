package auth.presentation.landing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
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
    Scaffold(
        modifier = Modifier.background(
            color = Color(0xFFE0EAFF)
        )
    ) { innerPadding ->
        when(deviceConfiguration) {
            DeviceConfiguration.PHONE_PORTRAIT -> {
                LandingContentPhonePortrait(
                    onLoginClick = onNavigateToLogin,
                    onRegisterClick = onNavigateToRegister,
                    modifier = Modifier.padding(
                        start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                        end = innerPadding.calculateEndPadding(LayoutDirection.Ltr),
                        top = 0.dp,
                        bottom = innerPadding.calculateBottomPadding()
                    )
                )
            }
            DeviceConfiguration.PHONE_LANDSCAPE -> {
                LandingContentPhoneLandscape(
                    onLoginClick = onNavigateToLogin,
                    onRegisterClick = onNavigateToRegister,
                    modifier = Modifier.padding(
                        start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                        end = innerPadding.calculateEndPadding(LayoutDirection.Ltr),
                        top = 0.dp,
                        bottom = innerPadding.calculateBottomPadding()
                    )
                )
            }
            DeviceConfiguration.TABLET_PORTRAIT -> {
                LandingContentTabletPortrait(
                    onLoginClick = onNavigateToLogin,
                    onRegisterClick = onNavigateToRegister,
                    modifier = Modifier.padding(
                        start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                        end = innerPadding.calculateEndPadding(LayoutDirection.Ltr),
                        top = 0.dp,
                        bottom = innerPadding.calculateBottomPadding()
                    )
                )
            }
            DeviceConfiguration.TABLET_LANDSCAPE-> {
                LandingContentTabletLandscape(
                    onLoginClick = onNavigateToLogin,
                    onRegisterClick = onNavigateToRegister,
                    modifier = Modifier.padding(
                        start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                        end = innerPadding.calculateEndPadding(LayoutDirection.Ltr),
                        top = 0.dp,
                        bottom = innerPadding.calculateBottomPadding()
                    )
                )
            }
            else -> Unit
        }
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