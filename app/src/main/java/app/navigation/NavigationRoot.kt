package app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import auth.presentation.landing.LandingScreen
import auth.presentation.login.LoginRoot
import auth.presentation.register.RegisterRoot
import notes.presentation.note_list.NoteListRoot

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
        composable<NavigationRoute.NoteList> {
            NoteListRoot()
        }
    }
}