package app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import auth.presentation.landing.LandingScreen
import auth.presentation.login.LoginRoot
import auth.presentation.register.RegisterRoot

@Composable
fun NavigationRoot(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoute.Landing
    ) {
        composable<NavigationRoute.Landing> {
            LandingScreen(
                navigateToLogin = { navController.navigate(NavigationRoute.Login) },
                navigateToRegister = { navController.navigate(NavigationRoute.Register) }
            )
        }
        composable<NavigationRoute.Login> {
            LoginRoot()
        }
        composable<NavigationRoute.Register> {
            RegisterRoot(
                navigateToLogin = { navController.navigate(NavigationRoute.Login) }
            )
        }
    }
}