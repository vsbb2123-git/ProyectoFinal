package com.vsantamaria.proyectofinal.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.vsantamaria.proyectofinal.R
import com.vsantamaria.proyectofinal.navigation.Routes

@Composable
fun PopUpLogin(
    navController: NavController,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {},///el onDismissRequest se tiene que quedar vacío por que si no si sse pulsa en cualquier sitio fuera del alertdialog se cierra
        title = { Text(text = stringResource(R.string.inicio_de_sesion_requerido)) },
        text = { Text(text = stringResource(R.string.quieres_iniciar_sesion_o_volver_atr_s)) },
        confirmButton = {
            Button(
                onClick = {
                    navController.navigate(Routes.OnBoarding.route)
                }
            ) {
                Text(stringResource(R.string.iniciar_sesi_n))
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onDismiss() ///cierra el popup y no hace nada
                }
            ) {
                Text(stringResource(R.string.volver))
            }
        }
    )
}
