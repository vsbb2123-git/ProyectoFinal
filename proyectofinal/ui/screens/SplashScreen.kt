package com.vsantamaria.proyectofinal.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.vsantamaria.proyectofinal.navigation.Routes
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.draw.alpha
import androidx.navigation.NavController

@Composable
fun SplashScreen(navController: NavHostController) {
    @Composable
    fun SplashScreen(navController: NavController) {
        /// Animación para la transparencia
        val alphaAnimation = rememberInfiniteTransition(label = "")/// no se por que me pide poner el label, pero si no me da errores
        val alpha = alphaAnimation.animateFloat(
            initialValue = 1f,
            targetValue = 0.6f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ), label = "" /// no se por que me pide poner el label, pero si no me da errores
        )

        LaunchedEffect(Unit) {
            kotlinx.coroutines.delay(1000L) /// 1 sec de delay
            navController.navigate(Routes.MainScreen.route)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center

        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle, /// desde aqui se cambia el icono
                contentDescription = "Splash Icon",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(100.dp)
                    .alpha(alpha.value)
            )
        }
    }
}