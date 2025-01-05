package com.vsantamaria.proyectofinal.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vsantamaria.proyectofinal.ui.theme.ProyectoFinalTheme


@Composable
fun AppContent(content: @Composable ()->Unit) {
    ProyectoFinalTheme{
        Surface(
            modifier= Modifier.fillMaxSize(),
            color= MaterialTheme.colorScheme.background
        ){
            content()
        }
    }
}