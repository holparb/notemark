package com.holparb.notemark.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.holparb.notemark.auth.presentation.landing.LandingScreen
import com.holparb.notemark.auth.presentation.login.LoginRoot
import com.holparb.notemark.auth.presentation.register.RegisterRoot
import com.holparb.notemark.notes.presentation.create_edit_note.CreateEditNoteRoot
import com.holparb.notemark.notes.presentation.note_list.NoteListRoot

@Composable
fun NavigationRoot(
    navController: NavHostController,
    isLoggedIn: Boolean
) {
    NavHost(
        navController = navController,
        startDestination = if(isLoggedIn) NavigationGroup.Notes else NavigationGroup.Auth
    ) {
        navigation<NavigationGroup.Auth>(
            startDestination = NavigationRoute.Landing::class
        ) {
            composable<NavigationRoute.Landing> {
                LandingScreen(
                    navigateToLogin = {
                        navController.navigate(NavigationRoute.Login) {
                            popUpTo(NavigationRoute.Landing) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    },
                    navigateToCreateAccount = {
                        navController.navigate(NavigationRoute.Register) {
                            popUpTo(NavigationRoute.Landing) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable<NavigationRoute.Login> {
                LoginRoot(
                    navigateToCreateAccount = {
                        navController.navigate(NavigationRoute.Register)
                    },
                    navigateToNoteList = {
                        navController.navigate(NavigationRoute.NoteList(navigateFromLogin = true)) {
                            popUpTo(NavigationGroup.Auth) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable<NavigationRoute.Register> {
                RegisterRoot(
                    navigateToLogin = {
                        navController.navigate(NavigationRoute.Login)
                    }
                )
            }
        }
        navigation<NavigationGroup.Notes>(
            startDestination = NavigationRoute.NoteList::class
        ) {
            composable<NavigationRoute.NoteList> {
                NoteListRoot(
                    navigateToCreateEditNote = { noteId ->
                        navController.navigate(NavigationRoute.CreateEditNote(noteId))
                    }
                )
            }
            composable<NavigationRoute.CreateEditNote> {
                CreateEditNoteRoot(
                    navigateBack = {
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}