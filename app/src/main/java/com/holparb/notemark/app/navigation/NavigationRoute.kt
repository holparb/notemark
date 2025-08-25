package com.holparb.notemark.app.navigation

import kotlinx.serialization.Serializable

sealed interface NavigationRoute {
    @Serializable
    data object Landing: NavigationRoute

    @Serializable
    data object Login: NavigationRoute

    @Serializable
    data object Register: NavigationRoute

    @Serializable
    data object NoteList: NavigationRoute

    @Serializable
    data class CreateEditNote(val noteId: String = ""): NavigationRoute
}