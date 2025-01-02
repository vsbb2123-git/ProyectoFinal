package com.vsantamaria.proyectofinal.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.vsantamaria.proyectofinal.navigation.Routes
import com.vsantamaria.proyectofinal.ui.viewmodels.OnBoardingViewModel

@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnBoarding(navController: NavHostController) {
    val context = LocalContext.current
    val onBoardingViewModel = remember {
        OnBoardingViewModel(context)
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            TextField(
//                value = username,
//                onValueChange = { onBoardingViewModel.onNameChange(it) },
//                label = { Text(text = "Nombre") }
//            )
//            Spacer(modifier = Modifier.height(16.dp))
//            TextField(
//                value = password,
//                onValueChange = { onBoardingViewModel.onPasswordChange(it) },
//                label = { Text("Contraseña") },
//                visualTransformation = PasswordVisualTransformation()
//
//            )
//            Spacer(modifier = Modifier.height(16.dp))
//            Button(onClick = {
//                onBoardingViewModel.saveUserData(
//                    username = username,
//                    password = password
//                )
//                navController.popBackStack()
//                navController.navigate(Routes.MainScreen.route)
//            }) {
//                Text("Continuar")
//            }
        }
    }
}
