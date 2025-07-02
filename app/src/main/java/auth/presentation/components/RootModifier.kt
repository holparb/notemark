package auth.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.rootModifier(innerPadding: PaddingValues): Modifier {
    return this
        .fillMaxSize()
        .padding(innerPadding)
        .clip(
            RoundedCornerShape(
                topStart = 20.dp,
                topEnd = 20.dp
            )
        )
        .background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
        .consumeWindowInsets(WindowInsets.navigationBars)
}