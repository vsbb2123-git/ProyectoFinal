package com.vsantamaria.proyectofinal.navigation

sealed class Routes(val route: String) {
    object SplashScreen : Routes("splash_screen")
    object OnBoarding : Routes("on_boarding")
    object MainScreen : Routes("main_screen")
    object AccountScreen : Routes("account_screen")
    object ShoppingCartScreen : Routes("game_card_screen")
}