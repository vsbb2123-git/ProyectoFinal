package com.vsantamaria.proyectofinal.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.vsantamaria.proyectofinal.R
import com.vsantamaria.proyectofinal.database.viewmodels.UsersViewModel
import com.vsantamaria.proyectofinal.navigation.Routes


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyScaffold(
    title: String,
    navController: NavController,
    usersViewModel: UsersViewModel,
    content: @Composable () -> Unit
) {
    var showDDMenu by rememberSaveable { mutableStateOf(false) }
    var logged by rememberSaveable { mutableStateOf(false) }
    val currentUser by usersViewModel.getCurrentUser().observeAsState()
    var showPopUp by remember { mutableStateOf(false) }

    LaunchedEffect(currentUser) {
        logged = currentUser != null
    }

    if (showPopUp) {
        PopUpLogin(
            navController = navController,
            onDismiss = { showPopUp = false }
        )
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
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if(title!= stringResource(R.string.lista_de_juegos)){
                            IconButton(
                                onClick = {
                                    navController.popBackStack()
                                    navController.navigate(Routes.MainScreen.route)
                                }
                            ){
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                        Text(title,color = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                actions = {

                    IconButton(onClick = { showDDMenu = !showDDMenu }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "menu",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    DropdownMenu(
                        expanded = showDDMenu,
                        onDismissRequest = { showDDMenu = false }
                    ) {

                        if (logged) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.cerrar_sesi_n)) },///asigna 0 al currentSession del user, cerrandole la sesion y vuelve a la pantalla principal
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
                                text = {Text(stringResource(R.string.iniciar_sesi_n_registrarse))},
                                onClick = {
                                    showDDMenu = false
                                    navController.navigate(Routes.OnBoarding.route)

                                }
                            )
                        }
                        if(title!= stringResource(R.string.informacion_de_la_cuenta)) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.cuenta)) },
                                onClick = {
                                    if (logged) {
                                        navController.popBackStack()
                                        navController.navigate(Routes.AccountScreen.route)
                                    }else {
                                        showPopUp = true
                                    }
                                }
                            )
                        }
                        if(title!= stringResource(R.string.juegos_favoritos)) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.lista_de_favoritos)) },
                                onClick = {
                                    if (logged) {
                                        navController.popBackStack()
                                        navController.navigate(Routes.WishListScreen.route)
                                    } else {
                                        showPopUp = true
                                    }
                                }
                            )
                        }

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
