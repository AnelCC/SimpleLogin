package com.anelcc.simplelogin.views.main

import android.app.Activity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.anelcc.simplelogin.views.SimpleLoginViewModel


@Composable
fun HomeScreen(navController: NavController, viewModel: SimpleLoginViewModel) {
    val activity = (LocalContext.current as? Activity)

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .verticalScroll(
                rememberScrollState()
            ),
            horizontalAlignment = Alignment.CenterHorizontally) {

            Text(text = "Hello, ${viewModel.userData.value?.name ?: "you"}! ",
                Modifier.padding(16.dp),
                fontSize = 30.sp,
                fontFamily = FontFamily.SansSerif
            )
            Text(text = "Your super-awesome portfolio has been successfully submitted! " +
                    "The details bellow will be public within your community",
                Modifier.padding(8.dp),
                fontSize = 12.sp,
                fontFamily = FontFamily.Serif
            )
            Text(text = viewModel.userData.value?.website ?: "webside not found",
                Modifier.padding(8.dp),
                fontSize = 18.sp,
                fontFamily = FontFamily.Serif
            )
            Text(text = viewModel.userData.value?.name ?: "You",
                Modifier.padding(8.dp),
                fontSize = 18.sp,
                fontFamily = FontFamily.Serif
            )
            Text(text = viewModel.userData.value?.email ?: "you@email.com",
                Modifier.padding(8.dp),
                fontSize = 18.sp,
                fontFamily = FontFamily.Serif
            )
            Button(onClick = {
                viewModel.onSignOut()
                activity?.finish()
            },
                modifier = Modifier.padding(8.dp)) {
                Text(text = "Log out")
            }
        }
    }

}