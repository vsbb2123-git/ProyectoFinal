package com.vsantamaria.proyectofinal.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vsantamaria.proyectofinal.database.daos.UsersDAO
//import com.vsantamaria.proyectofinal.ui.screens.OnBoarding
import com.vsantamaria.proyectofinal.ui.screens.MainScreen
import com.vsantamaria.proyectofinal.ui.screens.SplashScreen

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun Navigation(navController: NavHostController, usersDAO: UsersDAO) {
    NavHost(
        navController = navController,
        startDestination = Routes.SplashScreen.route
    ) {
        composable(
            route = Routes.SplashScreen.route
        ) {
            SplashScreen(navController)
        }

//        composable(
//            route = Routes.OnBoarding.route
//        ) {
//            OnBoarding(navController)
//        }

        composable(
            route = Routes.MainScreen.route
        ) {
            MainScreen(navController, usersDAO)
        }


    }
}


