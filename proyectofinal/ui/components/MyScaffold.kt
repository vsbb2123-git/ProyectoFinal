package com.vsantamaria.proyectofinal.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.vsantamaria.proyectofinal.database.viewmodels.UsersViewModel
import com.vsantamaria.proyectofinal.navigation.Routes


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyScaffold(
    modifier: Modifier = Modifier,
    title: String,
    navController: NavController,
    usersViewModel: UsersViewModel,
    content: @Composable () -> Unit
) {
    var showDDMenu by rememberSaveable { mutableStateOf(false) }
    var logged by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    val currentUser by usersViewModel.getCurrentUser().observeAsState()

    LaunchedEffect(currentUser) {
        logged = currentUser != null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.background(MaterialTheme.colorScheme.primary),
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(title)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {

                        }
                    }
                },
                actions = {

                    IconButton(onClick = { showDDMenu = !showDDMenu }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "menu"
                        )
                    }
                    DropdownMenu(
                        expanded = showDDMenu,
                        onDismissRequest = { showDDMenu = false }
                    ) {

                        DropdownMenuItem(
                            text = { Text("Pagina principal") },
                            onClick = {
                                navController.popBackStack()
                                navController.navigate(Routes.MainScreen.route)
                            }
                        )
                        if (logged) {
                            DropdownMenuItem(
                                text = { Text("Cerrar Sesión") },///asigna 0 al currentSession del user, cerrandole la sesion y vuelve a la pantalla principal
                                onClick = {
                                    currentUser?.let { user ->
                                        usersViewModel.logout(user.id)
                                        showDDMenu = false
                                        navController.popBackStack()
                                        navController.navigate(Routes.MainScreen.route)
                                    }
                                }
                            )
                        } else {
                            DropdownMenuItem(
                                text = { Text("Iniciar Sesión / Registrarse") },
                                onClick = {
                                    showDDMenu = false
                                    navController.navigate(Routes.OnBoarding.route)

                                }
                            )
                        }
                        DropdownMenuItem(
                            text = { Text("Cuenta") },
                            onClick = {
                                navController.popBackStack()
//                                navController.navigate(Routes.AccountScreen.route)
                            }
                        )
                    }
                }
            )
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            content()

        }

    }
}
