package com.holparb.notemark.app.navigation

import kotlinx.serialization.Serializable

sealed interface NavigationGroup {
    @Serializable
    data object Auth: NavigationGroup

    @Serializable
    data object Notes: NavigationGroup
}