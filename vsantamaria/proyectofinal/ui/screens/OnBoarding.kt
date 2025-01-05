package com.vsantamaria.proyectofinal.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.vsantamaria.proyectofinal.database.entities.Users
import com.vsantamaria.proyectofinal.database.viewmodels.UsersViewModel
import com.vsantamaria.proyectofinal.navigation.Routes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
                title = { Text("Inicio de Sesión") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )

            Box( /// para que no esté alineado al centro y no esté pegado a la parte superior
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
                    Text(text = "Iniciar Sesión", modifier = Modifier.padding(16.dp))
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
                    Text(text = "Registrarse", modifier = Modifier.padding(16.dp))
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
                    label = { Text(text = "Nombre de Usuario") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
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
                    label = { Text(text = "Nombre de Usuario") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
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
                    label = { Text("Confirmar Contraseña") },
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
                        label = { Text("Tipo de Usuario") },
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
                if (signUpMode == 0) Text("Iniciar sesion") else Text("Crear cuenta")
            }
        }
    }
}

