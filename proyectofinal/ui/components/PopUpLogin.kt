package com.vsantamaria.proyectofinal.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.vsantamaria.proyectofinal.navigation.Routes

@Composable
fun PopUpLogin(
    navController: NavController,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {},///el onDismissRequest se tiene que quedar vacío por que si no si sse pulsa en cualquier sitio fuera del alertdialog se cierra
        title = { Text(text = "Inicio de sesion requerido") },
        text = { Text(text = "Quieres iniciar sesion o volver atrás?") },
        confirmButton = {
            Button(
                onClick = {
                    navController.navigate(Routes.OnBoarding.route)
                }
            ) {
                Text("Iniciar Sesión")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onDismiss() ///cierra el popup y no hace nada
                }
            ) {
                Text("Volver")
            }
        }
    )
}
