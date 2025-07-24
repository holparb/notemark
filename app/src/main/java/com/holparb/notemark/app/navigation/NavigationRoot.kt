package com.holparb.notemark.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.holparb.notemark.auth.presentation.landing.LandingScreen
import com.holparb.notemark.auth.presentation.login.LoginRoot
import com.holparb.notemark.auth.presentation.register.RegisterRoot
import com.holparb.notemark.notes.presentation.note_list.NoteListRoot

@Composable
fun NavigationRoot(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = NavigationGroup.Auth
    ) {
        navigation<NavigationGroup.Auth>(
            startDestination = NavigationRoute.Landing
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
                        navController.navigate(NavigationGroup.Notes) {
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
            startDestination = NavigationRoute.NoteList()
        ) {
            composable<NavigationRoute.NoteList> {
                NoteListRoot()
            }
        }
    }
}