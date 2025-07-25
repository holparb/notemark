package com.holparb.notemark.auth.presentation.landing.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
fun LandingContentTabletLandscape(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
            .background(color = Color(0xFFE0EAFF)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.landing_image),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.FillBounds
        )
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerLowest,
                    shape = RoundedCornerShape(20.dp, 20.dp)
                )
                .width(IntrinsicSize.Max)
                .weight(1f)
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

@Preview(name = "Tablet Landscape", device = Devices.TABLET, widthDp = 1280, heightDp = 720)
@Composable
private fun LandingContentTabletLandscapePreview() {
    NoteMarkTheme {
        LandingContentTabletLandscape(
            onLoginClick = {},
            onRegisterClick = {}
        )
    }
}