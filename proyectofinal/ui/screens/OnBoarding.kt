package com.vsantamaria.proyectofinal.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.vsantamaria.proyectofinal.R
import com.vsantamaria.proyectofinal.database.viewmodels.UsersViewModel
import com.vsantamaria.proyectofinal.navigation.Routes
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnBoarding(navController: NavController, usersViewModel: UsersViewModel) {
    var signUpMode by remember { mutableStateOf(0) } ///iniciar sesion = 0  registrarse = 1
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var userType by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    var openUserType by remember { mutableStateOf(false) }
    val options = listOf("Usuario Base", "Crítico", "Desarrollador")
    val coroutineScope = rememberCoroutineScope()

    Surface(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopAppBar(
                title = { Text(stringResource(R.string.inicio_de_sesi_n),color = MaterialTheme.colorScheme.onPrimary) },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )

            Box( /// para que ni esté alineado al centro ni esté pegado a la parte superior
                Modifier.fillMaxHeight(0.15f)
            ) {}

            TabRow(selectedTabIndex = signUpMode) {
                Tab(
                    selected = signUpMode == 0,
                    onClick = {
                        signUpMode = 0
                        password = ""
                        errorMessage = ""
                    }
                ) {
                    Text(text = stringResource(R.string.iniciar_sesi_n), modifier = Modifier.padding(16.dp))
                }

                Tab(
                    selected = signUpMode == 1,
                    onClick = {
                        signUpMode = 1
                        password = ""
                        confirmPassword = ""
                        userType=""
                        errorMessage = ""
                    }

                ) {
                    Text(text = stringResource(R.string.registrarse), modifier = Modifier.padding(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (errorMessage.isNotEmpty()) {
                Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(16.dp))
            }


            if (signUpMode == 0) {

                TextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text(text = stringResource(R.string.nombre_de_usuario_onboarding)) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(stringResource(R.string.contrase_a_onboarding)) },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = {
                            passwordVisible = true
                            coroutineScope.launch {
                                delay(2000)
                                passwordVisible = false
                            }
                        }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = "Mostrar contraseña"
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

            } else {
                TextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text(text = stringResource(R.string.nombre_de_usuario_onboarding)) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(stringResource(R.string.contrase_a_onboarding)) },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = {
                            passwordVisible = true
                            coroutineScope.launch {
                                delay(2000)
                                passwordVisible = false
                            }
                        }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = "Mostrar contraseña"
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text(stringResource(R.string.confirmar_contrase_a)) },
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = {
                            confirmPasswordVisible = true
                            coroutineScope.launch {
                                delay(2000)
                                confirmPasswordVisible = false
                            }
                        }) {
                            Icon(
                                imageVector = if (confirmPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = "Mostrar contraseña"
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                ExposedDropdownMenuBox(
                    expanded = openUserType,
                    onExpandedChange = { openUserType = !openUserType }
                ) {
                    OutlinedTextField(
                        value = userType,
                        onValueChange = { },
                        label = { Text(stringResource(R.string.tipo_de_usuario)) },
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = openUserType)
                        },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = openUserType,
                        onDismissRequest = { openUserType = false }
                    ) {
                        options.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    userType = option
                                    openUserType = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            Button(onClick = {
                scope.launch {
                    val error = if (signUpMode == 0) {
                        usersViewModel.logIn(username, password)
                    } else {
                        usersViewModel.signIn(username, password, confirmPassword, userType)
                    }

                    if (error.isEmpty()) {
                        navController.navigate(Routes.MainScreen.route)
                    } else {
                        errorMessage = error
                    }
                }
            }) {
                if (signUpMode == 0) Text(stringResource(R.string.iniciar_sesion)) else Text(
                    stringResource(R.string.crear_cuenta)
                )
            }
        }
    }
}

