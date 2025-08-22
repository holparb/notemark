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
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val mainViewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                mainViewModel.isLoadingSessionData.value
            }
        }
        enableEdgeToEdge()
        setContent {
            NoteMarkTheme {
                val isLoggedIn by mainViewModel.isLoggedIn.collectAsStateWithLifecycle()
                NavigationRoot(
                    navController = rememberNavController(),
                    isLoggedIn = isLoggedIn
                )
            }
        }
    }
}