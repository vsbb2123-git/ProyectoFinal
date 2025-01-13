package com.vsantamaria.proyectofinal.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.vsantamaria.proyectofinal.R
import com.vsantamaria.proyectofinal.database.models.FullComment
import com.vsantamaria.proyectofinal.database.viewmodels.CommentsViewModel
import com.vsantamaria.proyectofinal.database.viewmodels.UsersViewModel
import com.vsantamaria.proyectofinal.navigation.Routes
import com.vsantamaria.proyectofinal.ui.components.CommentCard
import com.vsantamaria.proyectofinal.ui.components.MyScaffold
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AccountScreen(navController: NavController, usersViewModel: UsersViewModel, commentsViewModel: CommentsViewModel) {
    val scope = rememberCoroutineScope()
    val comments = remember { mutableStateOf(emptyList<FullComment>()) }
    val wishlistCount = remember { mutableStateOf(0) }
    val currentUser by usersViewModel.getCurrentUser().observeAsState()
    var passwordVisible by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var showPopUp by remember { mutableStateOf(false) }


    if (currentUser == null) {/// Si o si tiene que estar cargado el usuario
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(stringResource(R.string.cargando_detalles_del_usuario))
        }
        return /// no deja que "avance" la pantalla hasta que no se deje de cumplir el if
    }

    LaunchedEffect(currentUser!!.id) {
        scope.launch {
            comments.value = commentsViewModel.getCommentsByUser(currentUser!!.id)
            wishlistCount.value = currentUser!!.wishList.size
        }
    }
    if (showPopUp) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text(text = stringResource(R.string.est_s_a_punto_de_eliminar_completamente_tu_cuenta)) },
            text = { Text(text = stringResource(R.string.estas_seguro)) },
            confirmButton = {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            commentsViewModel.deleteCommentsByUserId(currentUser!!.id)
                            usersViewModel.deleteUserById(currentUser!!.id)

                            navController.popBackStack()
                            navController.navigate(Routes.MainScreen.route)
                        }
                        showPopUp=false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(stringResource(R.string.borrar),color = MaterialTheme.colorScheme.onError)
                }
            },
            dismissButton = {
                Button(
                    onClick = {showPopUp=false}
                ) {
                    Text(stringResource(R.string.volver))
                }
            }
        )
    }
    MyScaffold(
        title = "Informacion de la cuenta",
        navController = navController,
        usersViewModel = usersViewModel
    ) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.End
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f)
                    .padding(horizontal = 10.dp, vertical = 20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = stringResource(R.string.nombre_de_usuario, currentUser!!.username),
                    style = MaterialTheme.typography.bodyLarge
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val censoredPassword =
                        "*".repeat(currentUser!!.password.length) ///repite "*" por cada letra de la contraseña
                    Text(
                        text = if (passwordVisible) stringResource(
                            R.string.contrase_a,
                            currentUser!!.password
                        ) else stringResource(
                            R.string.contrase_a, censoredPassword), ///no se puede utilizar el passwordvisualtransformation en un text
                        style = MaterialTheme.typography.bodyLarge
                    )
                    IconButton(
                        onClick = {
                            passwordVisible = true
                            coroutineScope.launch {
                                delay(2000)
                                passwordVisible = false
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = stringResource(R.string.mostrar_contrase_a)
                        )
                    }

                }

                if (wishlistCount.value == 0) {///wishlist
                    Text(
                        text = stringResource(R.string.no_tienes_ningun_juego_en_tu_lista_de_favoritos),
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    Text(
                        text = stringResource(
                            R.string.juegos_en_tu_lista_de_favoritos,
                            wishlistCount.value
                        ),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                if (comments.value.isEmpty()) {///comentarios
                    Text(
                        text = stringResource(R.string.no_has_hecho_ning_n_comentario_a_n),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                } else {
                    Text(
                        text = stringResource(R.string.tus_comentarios),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    comments.value.forEach { comment ->
                        CommentCard(
                            comment = comment,
                            user = currentUser!!,
                            currentUser = currentUser,
                            onRemove = { commentToDelete ->
                                scope.launch {
                                    commentsViewModel.deleteCommentById(commentToDelete.id)
                                    comments.value = comments.value.filter { it.id != commentToDelete.id }
                                }
                            }
                        )
                    }
                }
            }
            Button(///boton para borrar la cuenta (borra los comentarios y despues el usuario)
                onClick = {showPopUp=true},
                Modifier.padding(horizontal = 10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text(stringResource(R.string.eliminar_cuenta), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onError)
            }

        }
    }
}