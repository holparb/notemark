package com.holparb.notemark.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.holparb.notemark.app.navigation.NavigationRoot
import com.holparb.notemark.app.presentation.MainViewModel
import com.holparb.notemark.core.presentation.designsystem.theme.NoteMarkTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NoteMarkTheme {
                val mainViewModel: MainViewModel = koinViewModel<MainViewModel>()
                val isLoggedIn by mainViewModel.isLoggedIn.collectAsStateWithLifecycle()
                NavigationRoot(
                    navController = rememberNavController(),
                    isLoggedIn = isLoggedIn
                )
            }
        }
    }
}