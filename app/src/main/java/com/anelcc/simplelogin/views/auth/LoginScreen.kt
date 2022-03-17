package com.anelcc.simplelogin.views.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.anelcc.simplelogin.DestinationScreen
import com.anelcc.simplelogin.views.SimpleLoginViewModel
import com.anelcc.simplelogin.views.main.CheckSignedIn
import com.anelcc.simplelogin.views.main.CommonProgressSpinner
import com.anelcc.simplelogin.views.main.navigateTo


@Composable
fun LoginScreen(navController: NavController, viewModel: SimpleLoginViewModel) {

    CheckSignedIn(viewModel = viewModel, navController = navController)

    val focus = LocalFocusManager.current

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .verticalScroll(
                rememberScrollState()
            ),
            horizontalAlignment = Alignment.CenterHorizontally) {

            val emailState = remember { mutableStateOf(TextFieldValue()) }
            val passState = remember { mutableStateOf(TextFieldValue()) }

            Text(
                text = "login",
                modifier = Modifier.padding(8.dp),
                fontSize = 30.sp,
                fontFamily = FontFamily.Serif
            )
            OutlinedTextField(
                value = emailState.value,
                onValueChange = { emailState.value = it },
                modifier = Modifier.padding(8.dp),
                label = { Text(text = "Email") })
            OutlinedTextField(
                value = passState.value,
                onValueChange = { passState.value = it },
                modifier = Modifier.padding(8.dp),
                label = { Text(text = "Password") })

            Button(onClick = {
                viewModel.onLogin(emailState.value.text, passState.value.text)
                focus.clearFocus(force = true)
            },
                modifier = Modifier.padding(8.dp)) {
                Text(text = "Login")
            }

            Text(text = "New Here? Go To Sign Up ->",
                color = Color.Blue,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        navigateTo(navController, DestinationScreen.SignUp)
                    }
            )

        }

        val isLoading = viewModel.isInProgress.value
        if (isLoading) {
            CommonProgressSpinner()
        }
    }
}
