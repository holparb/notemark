package auth.presentation.landing.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.holparb.notemark.R
import com.holparb.notemark.core.presentation.designsystem.theme.NoteMarkTheme

@Composable
fun LandingContentTabletPortrait(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
            .background(color = Color(0xFFE0EAFF))
    ) {
        Image(
            painter = painterResource(R.drawable.landing_image),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
        Row(
            Modifier
                .align(Alignment.BottomCenter)
                .width(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainerLowest,
                        shape = RoundedCornerShape(20.dp, 20.dp)
                    )
                    .padding(48.dp)
            ) {
                LandingBody(
                    onLoginClick = onLoginClick,
                    onRegisterClick = onRegisterClick,
                    centerText = true
                )
            }
        }
    }
}

@Preview(name = "Tablet Portrait", device = Devices.TABLET, widthDp = 720, heightDp = 1280)
@Composable
private fun LandingContentTabletPortraitPreview() {
    NoteMarkTheme {
        LandingContentTabletPortrait(
            onLoginClick = {},
            onRegisterClick = {}
        )
    }
}