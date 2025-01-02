package com.vsantamaria.proyectofinal.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.vsantamaria.proyectofinal.api.Client
import com.vsantamaria.proyectofinal.database.daos.UsersDAO
import com.vsantamaria.proyectofinal.repository.GamesRepository
import com.vsantamaria.proyectofinal.ui.screens.GameCardScreen
import com.vsantamaria.proyectofinal.ui.screens.MainScreen
import com.vsantamaria.proyectofinal.ui.screens.OnBoarding
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

        composable(
            route = Routes.OnBoarding.route
        ) {
            OnBoarding(navController)
        }

        composable(
            route = Routes.MainScreen.route
        ) {
            MainScreen(navController, usersDAO)
        }

        composable(
            route = Routes.GameCardScreen.route,
            arguments = listOf(
                navArgument("gameId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val gameId = backStackEntry.arguments?.getString("gameId")
            val gamesRepository = GamesRepository(Client.apiService)
            GameCardScreen(navController,gamesRepository, usersDAO, gameId)
        }


    }
}


