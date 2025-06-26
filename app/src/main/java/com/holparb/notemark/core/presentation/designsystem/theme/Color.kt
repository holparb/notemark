package com.holparb.notemark.core.presentation.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val OnSurface = Color(0xFF1B1B1C)
val OnSurfaceWithOpacity = Color(0x1E1B1B1C)
val Background = Color(0xFF58A1F8)
val OnSurfaceVariant = Color(0xFF535364)
val Surface = Color(0xFFEFEFF2)
val SurfaceLowest = Color(0xFFFFFFFF)
val Error = Color(0xFFE1294B)
val Primary = Color(0xFF5977F7)
val PrimaryWithOpacity = Color(0x195977F7)
val OnPrimary = Color(0xFFFFFFFF)
val OnPrimaryWithOpacity = Color(0x1EFFFFFF)

val ColorScheme.buttonGradient: Brush
    get() = Brush.verticalGradient(
        listOf(
            Color(0xFF58A1F8),
            Color(0xFF5A4CF7)
        )
    )

val ColorScheme.onSurfaceWithOpacity: Color
    get() = OnSurfaceWithOpacity