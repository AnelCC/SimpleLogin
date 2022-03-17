package com.anelcc.simplelogin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.anelcc.simplelogin.views.auth.LoginScreen
import com.anelcc.simplelogin.views.auth.SignupScreen
import com.anelcc.simplelogin.views.main.HomeScreen
import com.anelcc.simplelogin.views.main.NotificationMessage
import com.anelcc.simplelogin.ui.theme.SimpleLoginTheme
import com.anelcc.simplelogin.views.SimpleLoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleLoginTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    SimpleLoginApp()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SimpleLoginTheme {
        SimpleLoginApp()
    }
}

@Composable
fun SimpleLoginApp() {
    val viewModel = hiltViewModel<SimpleLoginViewModel>()
    val navController = rememberNavController()

    NotificationMessage(viewModel = viewModel())
    NavHost(navController = navController, startDestination = DestinationScreen.SignUp.route) {
        composable(DestinationScreen.SignUp.route) {
            SignupScreen(navController = navController, viewModel = viewModel)
        }
        composable(DestinationScreen.Login.route) {
            LoginScreen(navController = navController, viewModel = viewModel)
        }
        composable(DestinationScreen.Home.route) {
            HomeScreen(navController = navController, viewModel = viewModel)
        }
        composable(DestinationScreen.Logout.route) {
            navController.clearBackStack(DestinationScreen.Home.route)
            SignupScreen(navController = navController, viewModel = viewModel)
        }
    }
}

sealed class DestinationScreen(val route: String) {
    object SignUp: DestinationScreen("signUp")
    object Login: DestinationScreen("login")
    object Home: DestinationScreen("home")
    object Logout: DestinationScreen("logout")
}