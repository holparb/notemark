package com.holparb.notemark.core.presentation.util

import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass

enum class DeviceConfiguration {
    PHONE_PORTRAIT,
    PHONE_LANDSCAPE,
    TABLET_PORTRAIT,
    TABLET_LANDSCAPE,
    UNKNOWN;

    companion object {
        fun fromWindowSizeClass(windowSizeClass: WindowSizeClass): DeviceConfiguration {
            val widthClass = windowSizeClass.windowWidthSizeClass
            val heightClass = windowSizeClass.windowHeightSizeClass

            return when {
                widthClass == WindowWidthSizeClass.COMPACT
                        && heightClass == WindowHeightSizeClass.MEDIUM -> PHONE_PORTRAIT
                widthClass == WindowWidthSizeClass.COMPACT
                        && heightClass == WindowHeightSizeClass.EXPANDED -> PHONE_PORTRAIT
                widthClass == WindowWidthSizeClass.EXPANDED
                        && heightClass == WindowHeightSizeClass.COMPACT -> PHONE_LANDSCAPE
                widthClass == WindowWidthSizeClass.MEDIUM
                        && heightClass == WindowHeightSizeClass.MEDIUM -> TABLET_PORTRAIT
                widthClass == WindowWidthSizeClass.EXPANDED
                        && heightClass == WindowHeightSizeClass.MEDIUM -> TABLET_LANDSCAPE
                else -> UNKNOWN
            }
        }
    }
}